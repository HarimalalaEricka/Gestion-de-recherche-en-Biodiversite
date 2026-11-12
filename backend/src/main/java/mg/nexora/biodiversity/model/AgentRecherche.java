package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

import mg.nexora.biodiversity.model.keys.AgentRechercheKey;
@Entity
@IdClass(AgentRechercheKey.class)
@Table(name = "agent_recherche")
public class AgentRecherche {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agentid")
    private Agent agent;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rechercheid")
    private Recherche recherche;

    // orderrole supprimé : remplacé par roleCollaboration

    @Enumerated(EnumType.STRING)
    @Column(name = "role_collaboration", nullable = false, length = 30)
    private RoleCollaboration roleCollaboration = RoleCollaboration.CHERCHEUR_PRINCIPAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 20)
    private StatutCollaboration statut = StatutCollaboration.ACCEPTEE;

    // Simple Integer (et non une relation Utilisateur) : évite de sérialiser un Utilisateur
    // (mot de passe haché inclus) dans les réponses JSON de ce CRUD.
    @Column(name = "invite_par")
    private Integer inviteParId;

    @Column(name = "invite_le")
    private Instant inviteLe;

    @Column(name = "accepte_le")
    private Instant accepteLe;

    public Agent getAgent() { return agent; }

    public void setAgent(Agent agent) { this.agent = agent; }

    public Recherche getRecherche() { return recherche; }

    public void setRecherche(Recherche recherche) { this.recherche = recherche; }

    public RoleCollaboration getRoleCollaboration() { return roleCollaboration; }

    public void setRoleCollaboration(RoleCollaboration roleCollaboration) { this.roleCollaboration = roleCollaboration; }

    public StatutCollaboration getStatut() { return statut; }

    public void setStatut(StatutCollaboration statut) { this.statut = statut; }

    public Integer getInviteParId() { return inviteParId; }

    public void setInviteParId(Integer inviteParId) { this.inviteParId = inviteParId; }

    public Instant getInviteLe() { return inviteLe; }

    public void setInviteLe(Instant inviteLe) { this.inviteLe = inviteLe; }

    public Instant getAccepteLe() { return accepteLe; }

    public void setAccepteLe(Instant accepteLe) { this.accepteLe = accepteLe; }

}