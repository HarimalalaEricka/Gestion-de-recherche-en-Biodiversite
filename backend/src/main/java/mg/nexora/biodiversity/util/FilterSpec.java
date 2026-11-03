package mg.nexora.biodiversity.util;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

public class FilterSpec {

    // Paramètres d'URL qui ne sont pas des filtres de colonne ("q" = recherche globale)
    private static final Set<String> RESERVED = Set.of("page", "size", "sort", "q");

    // Colonnes interdites au filtrage ET à la recherche
    private static final Set<String> BLOCKED = Set.of("motDePasse");

    public static <T> Specification<T> from(Map<String, String> params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Colonnes autorisées = attributs réels de l'entité, sauf les bloqués
            Set<String> allowed = new HashSet<>();
            // Colonnes texte = celles où la recherche globale s'applique
            List<String> textFields = new ArrayList<>();

            root.getModel().getAttributes().forEach(a -> {
                String name = a.getName();
                if (BLOCKED.contains(name)) return;
                allowed.add(name);
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

            // 2) Filtres par colonne (role, statut, ...)
            params.forEach((key, value) -> {
                try {
                    if (RESERVED.contains(key)) return;
                    if (value == null || value.isBlank() || !allowed.contains(key)) return;

                    Class<?> javaType = root.get(key).getJavaType();
                    String v = value.trim();

                    if (javaType == String.class) {
                        predicates.add(cb.like(cb.lower(root.get(key)), "%" + v.toLowerCase() + "%"));
                    } else if (javaType.isEnum()) {
                        predicates.add(cb.equal(root.get(key), toEnum(javaType, v)));
                    } else if (javaType == Integer.class || javaType == int.class) {
                        predicates.add(cb.equal(root.get(key), Integer.valueOf(v)));
                    } else if (javaType == Long.class || javaType == long.class) {
                        predicates.add(cb.equal(root.get(key), Long.valueOf(v)));
                    } else if (javaType == Boolean.class || javaType == boolean.class) {
                        predicates.add(cb.equal(root.get(key), Boolean.valueOf(v)));
                    }
                } catch (IllegalArgumentException e) {
                    // Valeur invalide : filtre ignoré
                }
            });

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object toEnum(Class<?> type, String value) {
        return Enum.valueOf((Class<Enum>) type, value.toUpperCase());
    }
}