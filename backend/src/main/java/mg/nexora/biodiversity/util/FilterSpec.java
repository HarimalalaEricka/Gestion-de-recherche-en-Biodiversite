package mg.nexora.biodiversity.util;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

public class FilterSpec {

    // Paramètres d'URL qui ne sont pas des filtres de colonne ("q" = recherche globale)
    private static final Set<String> RESERVED = Set.of("page", "size", "sort", "q");

    // Colonnes interdites au filtrage ET à la recherche (à tous les niveaux d'un chemin)
    private static final Set<String> BLOCKED = Set.of("motDePasse");

    // Profondeur maximale d'un chemin imbriqué : "recherche.rechercheID" = 2
    private static final int MAX_DEPTH = 3;

    public static <T> Specification<T> from(Map<String, String> params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Colonnes texte = celles où la recherche globale s'applique
            List<String> textFields = new ArrayList<>();

            root.getModel().getAttributes().forEach(a -> {
                String name = a.getName();
                if (BLOCKED.contains(name)) return;
                if (a.getJavaType() == String.class) textFields.add(name);
            });

            // 1) Recherche globale : q cherché dans toutes les colonnes String
            String q = params.get("q");
            if (q != null && !q.isBlank() && !textFields.isEmpty()) {
                String like = "%" + q.trim().toLowerCase() + "%";
                Predicate[] ors = textFields.stream()
                        .map(f -> cb.like(cb.lower(root.<String>get(f)), like))
                        .toArray(Predicate[]::new);
                predicates.add(cb.or(ors));
            }

            // 2) Filtres par colonne (role, statut, recherche.rechercheID, ...)
            params.forEach((key, value) -> {
                try {
                    if (RESERVED.contains(key)) return;
                    if (value == null || value.isBlank()) return;

                    Path<?> path = resolve(root, key);
                    if (path == null) return;

                    Class<?> javaType = path.getJavaType();
                    String v = value.trim();

                    if (javaType == String.class) {
                        Expression<String> texte = path.as(String.class);
                        predicates.add(cb.like(cb.lower(texte), "%" + v.toLowerCase() + "%"));
                    } else if (javaType.isEnum()) {
                        predicates.add(cb.equal(path, toEnum(javaType, v)));
                    } else if (javaType == Integer.class || javaType == int.class) {
                        predicates.add(cb.equal(path, Integer.valueOf(v)));
                    } else if (javaType == Long.class || javaType == long.class) {
                        predicates.add(cb.equal(path, Long.valueOf(v)));
                    } else if (javaType == Boolean.class || javaType == boolean.class) {
                        predicates.add(cb.equal(path, Boolean.valueOf(v)));
                    }
                } catch (IllegalArgumentException e) {
                    // Attribut inconnu ou valeur invalide : filtre ignoré
                }
            });

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Résout "attribut" ou "relation.attribut" en chemin JPA.
     * Retourne null si le chemin est trop profond ou traverse une colonne bloquée.
     * Un attribut inconnu lève une IllegalArgumentException (filtre ignoré par l'appelant).
     */
    private static Path<?> resolve(Root<?> root, String key) {
        String[] parts = key.split("\\.");
        if (parts.length > MAX_DEPTH) return null;

        Path<?> path = root;
        for (String part : parts) {
            if (BLOCKED.contains(part)) return null;
            path = path.get(part);
        }
        return path;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object toEnum(Class<?> type, String value) {
        return Enum.valueOf((Class<Enum>) type, value.toUpperCase());
    }
}