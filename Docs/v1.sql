CREATE SCHEMA IF NOT EXISTS darwin_core;

-- ============================================================
-- 1. DATASET
-- Jeu de données / ensemble général de données
-- ============================================================

CREATE TABLE darwin_core.dataset (
    "datasetID" TEXT PRIMARY KEY,
    -- Identifiant du jeu de données

    "datasetName" TEXT NOT NULL,
    -- Nom du jeu de données

    "license" TEXT,
    -- Licence d'utilisation des données

    "rightsHolder" TEXT
    -- Titulaire des droits
);


-- ============================================================
-- 2. AGENT
-- Personne / organisation intervenant dans les données
-- ============================================================

CREATE TABLE darwin_core.agent (
    "agentID" TEXT PRIMARY KEY,
    -- Identifiant de l'agent

    "agentName" TEXT NOT NULL,
    -- Nom de la personne ou de l'organisation

    "agentType" TEXT
    -- Type d'agent : personne, organisation, logiciel, etc.
);


-- ============================================================
-- 3. LOCATION
-- Lieu géographique
-- ============================================================

CREATE TABLE darwin_core.location (
    "locationID" TEXT PRIMARY KEY,
    -- Identifiant du lieu

    "country" TEXT,
    -- Pays

    "stateProvince" TEXT,
    -- État / province / région

    "municipality" TEXT,
    -- Commune / municipalité

    "locality" TEXT,
    -- Lieu précis

    "decimalLatitude" NUMERIC(10,7),
    -- Latitude décimale

    "decimalLongitude" NUMERIC(10,7),
    -- Longitude décimale

    "geodeticDatum" TEXT
    -- Système géodésique utilisé
);


-- ============================================================
-- 4. EVENT
-- Événement de collecte / observation
-- ============================================================

CREATE TABLE darwin_core.event (
    "eventID" TEXT PRIMARY KEY,
    -- Identifiant de l'événement

    "datasetID" TEXT NOT NULL,
    -- Jeu de données auquel appartient l'événement

    "locationID" TEXT,
    -- Lieu où l'événement a eu lieu

    "eventDate" TEXT,
    -- Date de l'événement

    "samplingProtocol" TEXT,
    -- Méthode / protocole utilisé pour l'observation ou la collecte

    CONSTRAINT fk_event_dataset
        FOREIGN KEY ("datasetID")
        REFERENCES darwin_core.dataset ("datasetID"),

    CONSTRAINT fk_event_location
        FOREIGN KEY ("locationID")
        REFERENCES darwin_core.location ("locationID")
);


-- ============================================================
-- 5. OCCURRENCE
-- Observation / présence enregistrée
-- ============================================================

CREATE TABLE darwin_core.occurrence (
    "occurrenceID" TEXT PRIMARY KEY,
    -- Identifiant de l'occurrence

    "eventID" TEXT NOT NULL,
    -- Événement pendant lequel l'occurrence a été enregistrée

    "basisOfRecord" TEXT NOT NULL,
    -- Base de l'enregistrement :
    -- HumanObservation, MachineObservation, PreservedSpecimen, etc.

    "occurrenceStatus" TEXT,
    -- Statut de présence :
    -- present / absent

    "recordedByID" TEXT,
    -- Identifiant de l'agent ayant enregistré l'observation

    "individualCount" INTEGER,
    -- Nombre d'individus observés

    CONSTRAINT fk_occurrence_event
        FOREIGN KEY ("eventID")
        REFERENCES darwin_core.event ("eventID"),

    CONSTRAINT fk_occurrence_agent
        FOREIGN KEY ("recordedByID")
        REFERENCES darwin_core.agent ("agentID")
);


-- ============================================================
-- 6. TAXON
-- Classification taxonomique
-- ============================================================

CREATE TABLE darwin_core.taxon (
    "taxonID" TEXT PRIMARY KEY,
    -- Identifiant du taxon

    "scientificName" TEXT NOT NULL,
    -- Nom scientifique

    "taxonRank" TEXT,
    -- Rang taxonomique : species, genus, family, etc.

    "kingdom" TEXT,
    -- Règne

    "phylum" TEXT,
    -- Embranchement

    "class" TEXT,
    -- Classe

    "order" TEXT,
    -- Ordre

    "family" TEXT,
    -- Famille

    "genus" TEXT,
    -- Genre

    "specificEpithet" TEXT
    -- Épithète spécifique
);


-- ============================================================
-- 7. IDENTIFICATION
-- Identification scientifique de l'occurrence
-- ============================================================

CREATE TABLE darwin_core.identification (
    "identificationID" TEXT PRIMARY KEY,
    -- Identifiant de l'identification

    "occurrenceID" TEXT NOT NULL,
    -- Occurrence concernée

    "taxonID" TEXT,
    -- Taxon attribué à l'identification
    -- (FK utilisée pour notre modèle relationnel)

    "identifiedByID" TEXT,
    -- Identifiant de l'agent qui a réalisé l'identification

    "dateIdentified" DATE,
    -- Date de l'identification

    "identificationQualifier" TEXT,
    -- Précision / doute sur l'identification :
    -- cf., aff., etc.

    "isAcceptedIdentification" BOOLEAN,
    -- Indique si cette identification est actuellement retenue

    CONSTRAINT fk_identification_occurrence
        FOREIGN KEY ("occurrenceID")
        REFERENCES darwin_core.occurrence ("occurrenceID"),

    CONSTRAINT fk_identification_taxon
        FOREIGN KEY ("taxonID")
        REFERENCES darwin_core.taxon ("taxonID"),

    CONSTRAINT fk_identification_agent
        FOREIGN KEY ("identifiedByID")
        REFERENCES darwin_core.agent ("agentID")
);


-- ============================================================
-- INDEX POUR LES RELATIONS
-- ============================================================

CREATE INDEX idx_event_dataset
    ON darwin_core.event ("datasetID");

CREATE INDEX idx_event_location
    ON darwin_core.event ("locationID");

CREATE INDEX idx_occurrence_event
    ON darwin_core.occurrence ("eventID");

CREATE INDEX idx_occurrence_agent
    ON darwin_core.occurrence ("recordedByID");

CREATE INDEX idx_identification_occurrence
    ON darwin_core.identification ("occurrenceID");

CREATE INDEX idx_identification_taxon
    ON darwin_core.identification ("taxonID");

CREATE INDEX idx_identification_agent
    ON darwin_core.identification ("identifiedByID");


-- ============================================================
-- CLASSES SUPPLEMENTAIRES DARWIN CORE
-- A executer APRES les 7 classes principales
-- ============================================================


-- ============================================================
-- 1. ORGANISM
-- Organisme / individu ou groupe suivi
-- ============================================================

CREATE TABLE darwin_core.organism (
    "organismID" TEXT PRIMARY KEY,
    -- Identifiant de l'organisme

    "organismName" TEXT,
    -- Nom donné à l'organisme / individu / groupe

    "organismScope" TEXT,
    -- Type ou portée de l'organisme :
    -- individu, groupe, colonie, etc.

    "causeOfDeath" TEXT,
    -- Cause du décès, si connue

    "organismRemarks" TEXT
    -- Remarques concernant l'organisme
);


-- ============================================================
-- RELATION OCCURRENCE -> ORGANISM
-- ============================================================
-- individualID est le terme Darwin Core utilisé pour identifier
-- l'organisme/individu associé à une occurrence.

ALTER TABLE darwin_core.occurrence
ADD COLUMN "individualID" TEXT;

ALTER TABLE darwin_core.occurrence
ADD CONSTRAINT fk_occurrence_organism
FOREIGN KEY ("individualID")
REFERENCES darwin_core.organism ("organismID");


-- ============================================================
-- 2. MEDIA
-- Image / vidéo / audio / autre média
-- Namespace : Audiovisual Core (ac:)
-- ============================================================

CREATE TABLE darwin_core.media (
    "mediaID" TEXT PRIMARY KEY,
    -- Identifiant du média
    -- Choix de conception pour identifier notre enregistrement

    "accessURI" TEXT,
    -- URI permettant d'accéder au média

    "associatedObservationReference" TEXT,
    -- Référence vers l'observation associée

    "providerLiteral" TEXT,
    -- Nom du fournisseur du média

    "caption" TEXT,
    -- Légende du média

    "captureDevice" TEXT,
    -- Appareil utilisé pour capturer le média

    "mediaDuration" NUMERIC,
    -- Durée du média en secondes

    "comments" TEXT
    -- Commentaires sur le média
);


-- ============================================================
-- RELATION MEDIA -> OCCURRENCE
-- ============================================================

ALTER TABLE darwin_core.media
ADD CONSTRAINT fk_media_occurrence
FOREIGN KEY ("associatedObservationReference")
REFERENCES darwin_core.occurrence ("occurrenceID");


-- ============================================================
-- 3. MATERIAL SAMPLE
-- Échantillon matériel
-- ============================================================

CREATE TABLE darwin_core.material_sample (
    "materialSampleID" TEXT PRIMARY KEY
    -- Identifiant de l'échantillon matériel
);


-- ============================================================
-- RELATION MATERIAL SAMPLE -> OCCURRENCE
-- ============================================================
-- Cette relation est un choix de modélisation pour notre base.
-- Darwin Core ne force pas cette FK SQL.

ALTER TABLE darwin_core.material_sample
ADD COLUMN "occurrenceID" TEXT;

ALTER TABLE darwin_core.material_sample
ADD CONSTRAINT fk_material_sample_occurrence
FOREIGN KEY ("occurrenceID")
REFERENCES darwin_core.occurrence ("occurrenceID");


-- ============================================================
-- 4. MEASUREMENT OR FACT
-- Mesure ou fait
-- ============================================================

CREATE TABLE darwin_core.measurement_or_fact (
    "measurementID" TEXT PRIMARY KEY,
    -- Identifiant de la mesure ou du fait

    "measurementType" TEXT NOT NULL,
    -- Type de mesure :
    -- poids, taille, température, etc.

    "measurementValue" TEXT,
    -- Valeur de la mesure

    "measurementUnit" TEXT,
    -- Unité :
    -- cm, kg, °C, etc.

    "measurementAccuracy" TEXT,
    -- Précision de la mesure

    "measurementMethod" TEXT,
    -- Méthode utilisée pour effectuer la mesure

    "measurementDeterminedBy" TEXT,
    -- Personne ayant déterminé la mesure

    "measurementDeterminedDate" DATE,
    -- Date de détermination de la mesure

    "measurementRemarks" TEXT,
    -- Remarques sur la mesure

    "parentMeasurementID" TEXT,
    -- Identifiant d'une mesure plus générale
    -- regroupant éventuellement plusieurs mesures

    CONSTRAINT fk_parent_measurement
        FOREIGN KEY ("parentMeasurementID")
        REFERENCES darwin_core.measurement_or_fact ("measurementID")
);


-- ============================================================
-- RELATION MEASUREMENT -> OCCURRENCE
-- ============================================================
-- Choix de conception pour notre cas d'utilisation :
-- les mesures sont principalement liées aux occurrences.

ALTER TABLE darwin_core.measurement_or_fact
ADD COLUMN "occurrenceID" TEXT;

ALTER TABLE darwin_core.measurement_or_fact
ADD CONSTRAINT fk_measurement_occurrence
FOREIGN KEY ("occurrenceID")
REFERENCES darwin_core.occurrence ("occurrenceID");


-- ============================================================
-- 5. GEOLOGICAL CONTEXT
-- Contexte géologique
-- ============================================================

CREATE TABLE darwin_core.geological_context (
    "geologicalContextID" TEXT PRIMARY KEY,
    -- Identifiant du contexte géologique

    "group" TEXT,
    -- Groupe géologique

    "formation" TEXT,
    -- Formation géologique

    "member" TEXT,
    -- Membre géologique

    "bed" TEXT,
    -- Couche / lit géologique

    "earliestPeriodOrLowestSystem" TEXT,
    -- Période géologique la plus ancienne / système inférieur

    "latestPeriodOrHighestSystem" TEXT,
    -- Période géologique la plus récente / système supérieur

    "earliestAgeOrLowestStage" TEXT,
    -- Âge / étage géologique le plus ancien

    "latestAgeOrHighestStage" TEXT
    -- Âge / étage géologique le plus récent
);


-- ============================================================
-- RELATION GEOLOGICAL CONTEXT -> OCCURRENCE
-- ============================================================
-- Choix de conception pour rattacher le contexte géologique
-- à l'occurrence.

ALTER TABLE darwin_core.occurrence
ADD COLUMN "geologicalContextID" TEXT;

ALTER TABLE darwin_core.occurrence
ADD CONSTRAINT fk_occurrence_geological_context
FOREIGN KEY ("geologicalContextID")
REFERENCES darwin_core.geological_context ("geologicalContextID");


-- ============================================================
-- 6. ORGANISM INTERACTION
-- Interaction entre deux organismes
-- ============================================================

CREATE TABLE darwin_core.organism_interaction (
    "organismInteractionID" TEXT PRIMARY KEY,
    -- Identifiant de l'interaction

    "organismInteractionType" TEXT,
    -- Type d'interaction :
    -- prédation, parasitisme, pollinisation, etc.

    "organismInteractionDescription" TEXT,

    "eventID" TEXT,
    -- Événement pendant lequel l'interaction a été observée

    "subjectOccurrenceID" TEXT,
    -- Occurrence représentant l'organisme qui agit

    "relatedOccurrenceID" TEXT
    -- Occurrence représentant l'organisme qui subit l'action
);


-- ============================================================
-- RELATIONS ORGANISM INTERACTION
-- ============================================================

ALTER TABLE darwin_core.organism_interaction
ADD CONSTRAINT fk_interaction_event
FOREIGN KEY ("eventID")
REFERENCES darwin_core.event ("eventID");

ALTER TABLE darwin_core.organism_interaction
ADD CONSTRAINT fk_interaction_subject_occurrence
FOREIGN KEY ("subjectOccurrenceID")
REFERENCES darwin_core.occurrence ("occurrenceID");

ALTER TABLE darwin_core.organism_interaction
ADD CONSTRAINT fk_interaction_related_occurrence
FOREIGN KEY ("relatedOccurrenceID")
REFERENCES darwin_core.occurrence ("occurrenceID");


-- ============================================================
-- 7. RESOURCE RELATIONSHIP
-- Relation entre deux ressources
-- ============================================================

CREATE TABLE darwin_core.resource_relationship (
    "resourceRelationshipID" TEXT PRIMARY KEY,
    -- Identifiant de la relation

    "resourceID" TEXT NOT NULL,
    -- Identifiant de la première ressource

    "relationshipOfResource" TEXT,
    -- Nature / rôle de la première ressource
    -- dans la relation

    "relationshipOfResourceID" TEXT,
    -- Identifiant de la ressource qui établit la relation

    "relatedResourceID" TEXT NOT NULL,
    -- Identifiant de la ressource liée

    "relatedResourceType" TEXT,
    -- Type de la ressource liée :
    -- Organism, Occurrence, MaterialSample, etc.

    "relationshipRemarks" TEXT,
    -- Remarques sur la relation

    "relationshipAccordingTo" TEXT,
    -- Source/personne selon laquelle la relation est établie

    "relationshipEstablishedDate" DATE
    -- Date à laquelle la relation a été établie
);


---------------------------
---- Specialisation 
-----------------------------
-- ============================================================
-- 3e PARTIE - CLASSES DARWIN CORE SPECIALISEES
-- PostgreSQL
-- A ajouter APRES les parties 1 et 2
-- ============================================================


-- ============================================================
-- 1. MATERIAL ENTITY
-- Entité matérielle
-- ============================================================
-- Représente une chose physique identifiable :
-- spécimen, fossile, partie d'organisme, matériel biologique, etc.

CREATE TABLE darwin_core.material_entity (
    "materialEntityID" TEXT PRIMARY KEY,
    -- Identifiant de l'entité matérielle

    "materialEntityType" TEXT,
    -- Type de matériel :
    -- spécimen, fossile, échantillon, partie d'organisme, etc.

    "materialEntityCategory" TEXT,
    -- Catégorie du matériel

    "catalogNumber" TEXT,
    -- Numéro de catalogue

    "disposition" TEXT,
    -- Situation du matériel :
    -- conservé, détruit, prêté, etc.

    "preparations" TEXT,
    -- Préparation / méthode de préparation

    "materialEntityRemarks" TEXT
    -- Remarques sur l'entité matérielle
);


-- ============================================================
-- 2. MATERIAL CITATION
-- Référence à un matériel dans une publication scientifique
-- ============================================================

CREATE TABLE darwin_core.material_citation (
    "materialCitationID" TEXT PRIMARY KEY,
    -- Identifiant de notre enregistrement de citation

    "referenceID" TEXT,
    -- Référence de la publication / source

    "materialEntityID" TEXT,
    -- Entité matérielle concernée

    "referenceRemarks" TEXT
    -- Remarques sur la référence

);


-- Relation MaterialCitation -> MaterialEntity

ALTER TABLE darwin_core.material_citation
ADD CONSTRAINT fk_material_citation_material
FOREIGN KEY ("materialEntityID")
REFERENCES darwin_core.material_entity ("materialEntityID");


-- ============================================================
-- 3. PROTOCOL
-- Protocole / méthode générale
-- ============================================================

CREATE TABLE darwin_core.protocol (
    "protocolID" TEXT PRIMARY KEY,
    -- Identifiant du protocole

    "protocolType" TEXT,
    -- Type de protocole

    "protocolDescription" TEXT,
    -- Description de la méthode

    "protocolReferences" TEXT,
    -- Références bibliographiques du protocole

    "protocolRemarks" TEXT
    -- Remarques sur le protocole
);


-- ============================================================
-- Relation EVENT -> PROTOCOL
-- ============================================================
-- Le terme Darwin Core samplingProtocol décrit le protocole
-- utilisé pendant un Event.
-- Ici, protocolID est une FK de notre modèle relationnel.

ALTER TABLE darwin_core.event
ADD COLUMN "protocolID" TEXT;

ALTER TABLE darwin_core.event
ADD CONSTRAINT fk_event_protocol
FOREIGN KEY ("protocolID")
REFERENCES darwin_core.protocol ("protocolID");


-- ============================================================
-- 4. PROVENANCE
-- Provenance / origine du projet ou financement
-- ============================================================

CREATE TABLE darwin_core.provenance (
    "provenanceID" TEXT PRIMARY KEY,
    -- Identifiant de la provenance

    "projectID" TEXT,
    -- Identifiant du projet

    "projectTitle" TEXT,
    -- Titre du projet

    "fundingAttributionID" TEXT,
    -- Identifiant de l'attribution du financement

    "fundingAttribution" TEXT
    -- Information sur le financement
);


-- ============================================================
-- 5. MOLECULAR PROTOCOL
-- Protocole moléculaire
-- ============================================================
-- Méthode utilisée pour effectuer une analyse moléculaire.

CREATE TABLE darwin_core.molecular_protocol (
    "molecularProtocolID" TEXT PRIMARY KEY,
    -- Identifiant du protocole moléculaire

    "assayType" TEXT
    -- Type d'analyse / assay
);


-- ============================================================
-- 6. NUCLEOTIDE ANALYSIS
-- Analyse nucléotidique
-- ============================================================
-- Résultats / informations quantitatives d'une analyse
-- de séquences.

CREATE TABLE darwin_core.nucleotide_analysis (
    "nucleotideAnalysisID" TEXT PRIMARY KEY,
    -- Identifiant de l'analyse
    -- Choix de conception permettant de relier les résultats

    "molecularProtocolID" TEXT,
    -- Protocole moléculaire utilisé

    "readCount" BIGINT,
    -- Nombre de lectures produites

    "processedTotalReadCount" BIGINT
    -- Nombre total de lectures après traitement
);


ALTER TABLE darwin_core.nucleotide_analysis
ADD CONSTRAINT fk_nucleotide_analysis_protocol
FOREIGN KEY ("molecularProtocolID")
REFERENCES darwin_core.molecular_protocol ("molecularProtocolID");


-- ============================================================
-- 7. NUCLEOTIDE SEQUENCE
-- Séquence nucléotidique
-- ============================================================

CREATE TABLE darwin_core.nucleotide_sequence (
    "nucleotideSequenceID" TEXT PRIMARY KEY,
    -- Identifiant de la séquence
    -- Choix de conception pour identifier notre séquence

    "nucleotideAnalysisID" TEXT,
    -- Analyse ayant produit la séquence

    "sequence" TEXT NOT NULL,
    -- Séquence ADN / ARN

    "nucleotideSequenceRemarks" TEXT
    -- Remarques sur la séquence
);


ALTER TABLE darwin_core.nucleotide_sequence
ADD CONSTRAINT fk_sequence_analysis
FOREIGN KEY ("nucleotideAnalysisID")
REFERENCES darwin_core.nucleotide_analysis ("nucleotideAnalysisID");


-- ============================================================
-- 8. BIBLIOGRAPHIC RESOURCE
-- Ressource bibliographique
-- ============================================================

CREATE TABLE darwin_core.bibliographic_resource (
    "referenceID" TEXT PRIMARY KEY,
    -- Identifiant de la référence

    "referenceType" TEXT,
    -- Type de référence :
    -- article, livre, rapport, etc.

    "referenceRemarks" TEXT
    -- Remarques sur la référence
);


-- ============================================================
-- 9. ASSERTION
-- Assertion / affirmation concernant une ressource
-- ============================================================

CREATE TABLE darwin_core.assertion (
    "assertionID" TEXT PRIMARY KEY,
    -- Identifiant de l'assertion

    "assertionType" TEXT,
    -- Type d'affirmation

    "assertionValue" TEXT,
    -- Valeur / contenu de l'affirmation

    "assertionBy" TEXT,
    -- Personne ou organisme ayant fait l'affirmation

    "assertionMadeDate" DATE,
    -- Date à laquelle l'affirmation a été faite

    "assertionEffectiveDate" DATE,
    -- Date à partir de laquelle elle est effective

    "assertionRemarks" TEXT
    -- Remarques
);


-- ============================================================
-- INDEX
-- ============================================================

CREATE INDEX idx_material_citation_material
    ON darwin_core.material_citation ("materialEntityID");

CREATE INDEX idx_event_protocol
    ON darwin_core.event ("protocolID");

CREATE INDEX idx_nucleotide_analysis_protocol
    ON darwin_core.nucleotide_analysis ("molecularProtocolID");

CREATE INDEX idx_nucleotide_sequence_analysis
    ON darwin_core.nucleotide_sequence ("nucleotideAnalysisID");