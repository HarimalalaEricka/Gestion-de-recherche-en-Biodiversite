CREATE TYPE AgentType AS ENUM (
   'PERSON',
   'GROUP',
   'INSTITUTION'
);

CREATE TYPE basisOfRecord AS ENUM (
   'FOSSIL_SPECIMEN',
   'HUMAN_OBSERVATION',
   'MACHINE_OBSERVATION',
   'PRESERVED_SPECIMEN',
   'LIVING_SPECIMEN',
   'MATERIAL_SPECIMEN',
   'MATERIAL_SAMPLE'
);

CREATE TYPE OccurenceStatus AS ENUM (
   'DETECTED',
   'NOT_DETECTED'
);

CREATE TYPE organismScope AS ENUM (
   'INDIVIDUAL',
   'GROUP',
   'COLONY',
   'POPULATION'
);

CREATE TYPE Confidentiality AS ENUM (
   'PUBLIC',
   'PRIVATE'
);

CREATE TYPE type_media AS ENUM (
   'IMAGE',
   'VIDEO',
   'AUDIO',
   'DOCUMENT'
);

CREATE TYPE role AS ENUM (
   'ADMIN',
   'RESEARCHER',
   'VIEWER',
   'DATA_ENTRY'
);

CREATE TYPE referenceType AS ENUM (
   'BOOK',
   'JOURNAL_ARTICLE',
   'WEBSITE',
   'REPORT',
   'THESIS',
   'CONFERENCE_PAPER',
   'DATASET',
   'OTHER'
);

CREATE TABLE Dataset(
   datasetID SERIAL,
   datasetName VARCHAR(500)  NOT NULL,
   Licence TEXT,
   rightsHolder TEXT,
   rechercheID INTEGER NOT NULL,
   PRIMARY KEY(datasetID),
   FOREIGN KEY(rechercheID) REFERENCES recherche(rechercheID)
);

CREATE TABLE Pays(
   PaysID SERIAL,
   pays VARCHAR(50) ,
   PRIMARY KEY(PaysID)
);

CREATE TABLE Province(
   ProvinceID SERIAL,
   province VARCHAR(250)  NOT NULL,
   PaysID INTEGER NOT NULL,
   PRIMARY KEY(ProvinceID),
   FOREIGN KEY(PaysID) REFERENCES Pays(PaysID)
);

CREATE TABLE taxonRank(
   taxonRankID SERIAL,
   taxonRank VARCHAR(250)  NOT NULL,
   PRIMARY KEY(taxonRankID)
);

CREATE TABLE measurementType(
   measurementTypeID SERIAL,
   measurementType VARCHAR(250)  NOT NULL,
   PRIMARY KEY(measurementTypeID),
   UNIQUE(measurementType)
);

CREATE TABLE measurementUnit(
   measurementUnitID SERIAL,
   measurementUnit VARCHAR(250)  NOT NULL,
   PRIMARY KEY(measurementUnitID),
   UNIQUE(measurementUnit)
);

CREATE TABLE geological_context(
   geological_contextID SERIAL,
   group_ TEXT,
   formation TEXT,
   member_ TEXT,
   bed TEXT,
   earliestPeriodOrLowestSystem TEXT,
   latestPeriodOrHighestSystem TEXT,
   earliestAgeOrLowestStage TEXT,
   latestAgeOrHighestStage TEXT,
   PRIMARY KEY(geological_contextID)
);

CREATE TABLE organismInteractionType(
   organismInteractionTypeID SERIAL,
   type VARCHAR(500) ,
   PRIMARY KEY(organismInteractionTypeID)
);

CREATE TABLE protocolType(
   protocolTypeID SERIAL,
   type VARCHAR(50) ,
   PRIMARY KEY(protocolTypeID)
);

CREATE TABLE NucleotideSequence(
   NucleotideSequenceID SERIAL,
   sequence_ TEXT NOT NULL,
   nucleotideSequenceRemarks_ TEXT,
   PRIMARY KEY(NucleotideSequenceID),
   UNIQUE(sequence_)
);

CREATE TABLE materialEntityType(
   materialEntityTypeID SERIAL,
   type VARCHAR(50) ,
   PRIMARY KEY(materialEntityTypeID)
);

CREATE TABLE materialEntityCategory(
   materialEntityCategoryID SERIAL,
   category VARCHAR(50) ,
   PRIMARY KEY(materialEntityCategoryID)
);

CREATE TABLE Agent(
   agentID SERIAL,
   agentRemarks TEXT,
   agentType AgentType NOT NULL,
   PRIMARY KEY(agentID)
);

CREATE TABLE Location(
   LocationID SERIAL,
   municipality VARCHAR(250)  NOT NULL,
   locality TEXT,
   decimalLatitude NUMERIC(10,7)  ,
   decimalLongitude NUMERIC(10,7)  ,
   geodeticDatum TEXT,
   ProvinceID INTEGER NOT NULL,
   PRIMARY KEY(LocationID),
   FOREIGN KEY(ProvinceID) REFERENCES Province(ProvinceID)
);

CREATE TABLE Taxon(
   TaxonID SERIAL,
   scientificName VARCHAR(500)  NOT NULL,
   kingdom VARCHAR(250) ,
   phylum VARCHAR(250) ,
   class VARCHAR(250) ,
   order_ VARCHAR(250) ,
   family VARCHAR(250) ,
   genus VARCHAR(250) ,
   specificEpithet VARCHAR(250) ,
   taxonRankID INTEGER NOT NULL,
   PRIMARY KEY(TaxonID),
   FOREIGN KEY(taxonRankID) REFERENCES taxonRank(taxonRankID)
);

CREATE TABLE Organism(
   OrganismID SERIAL,
   organismName VARCHAR(250) ,
   causeOfDeath TEXT,
   organismRemarks TEXT,
   organismScope organismScope NOT NULL,
   PRIMARY KEY(OrganismID)
);

CREATE TABLE Utilisateur(
   UtilisateurID SERIAL,
   nom VARCHAR(250)  NOT NULL,
   prenom VARCHAR(250) ,
   telephone VARCHAR(50) ,
   email VARCHAR(250)  NOT NULL,
   mot_de_passe VARCHAR(500)  NOT NULL,
   role role NOT NULL,
   PRIMARY KEY(UtilisateurID),
   UNIQUE(email)
);

CREATE TABLE recherche(
   rechercheID SERIAL,
   sujet VARCHAR(250) ,
   description TEXT,
   date_debut DATE NOT NULL,
   date_creation TIMESTAMP NOT NULL,
   date_fin DATE,
   Note_perso TEXT,
   Confidentialite Confidentiality NOT NULL,
   PRIMARY KEY(rechercheID)
);

CREATE TABLE Media_recherche(
   Media_rechercheID SERIAL,
   accessURI TEXT,
   caption TEXT,
   comments TEXT,
   UtilisateurID INTEGER NOT NULL,
   rechercheID INTEGER NOT NULL,
   type_media type_media NOT NULL,
   PRIMARY KEY(Media_rechercheID),
   FOREIGN KEY(UtilisateurID) REFERENCES Utilisateur(UtilisateurID),
   FOREIGN KEY(rechercheID) REFERENCES recherche(rechercheID),
);

CREATE TABLE Protocol(
   ProtocolID SERIAL,
   protocolName VARCHAR(500) ,
   protocolDescription TEXT,
   protocolReferences TEXT,
   protocolRemarks TEXT,
   protocolTypeID INTEGER NOT NULL,
   PRIMARY KEY(ProtocolID),
   FOREIGN KEY(protocolTypeID) REFERENCES protocolType(protocolTypeID)
);

CREATE TABLE MolecularProtocol(
   MolecularProtocolID SERIAL,
   assayType VARCHAR(250) ,
   molecularProtocolRemarks TEXT,
   ProtocolID INTEGER NOT NULL,
   PRIMARY KEY(MolecularProtocolID),
   FOREIGN KEY(ProtocolID) REFERENCES Protocol(ProtocolID)
);

CREATE TABLE MaterialEntity(
   MaterialEntityID SERIAL,
   institutionCode VARCHAR(250) ,
   collectionCode VARCHAR(250) ,
   catalogNumber VARCHAR(250) ,
   preparations TEXT,
   disposition VARCHAR(250) ,
   materialEntityRemarks TEXT,
   materialEntityCategoryID INTEGER NOT NULL,
   materialEntityTypeID INTEGER NOT NULL,
   PRIMARY KEY(MaterialEntityID),
   FOREIGN KEY(materialEntityCategoryID) REFERENCES materialEntityCategory(materialEntityCategoryID),
   FOREIGN KEY(materialEntityTypeID) REFERENCES materialEntityType(materialEntityTypeID)
);

CREATE TABLE bibliographic_resource(
   referenceID SERIAL,
   referenceRemarks TEXT,
   reference TEXT,
   referenceType referenceType NOT NULL,
   PRIMARY KEY(referenceID),
);

CREATE TABLE Event(
   EventID SERIAL,
   eventDate TIMESTAMP,
   samplingProtocol TEXT,
   ProtocolID INTEGER,
   LocationID INTEGER NOT NULL,
   datasetID INTEGER NOT NULL,
   PRIMARY KEY(EventID),
   FOREIGN KEY(ProtocolID) REFERENCES Protocol(ProtocolID),
   FOREIGN KEY(LocationID) REFERENCES Location(LocationID),
   FOREIGN KEY(datasetID) REFERENCES Dataset(datasetID)
);

CREATE TABLE Occurrence(
   OccurrenceID SERIAL,
   individualCount INTEGER,
   geological_contextID INTEGER,
   OrganismID INTEGER NOT NULL,
   occurrenceStatus OccurenceStatus NOT NULL,
   basisOfRecord basisOfRecord NOT NULL,
   EventID INTEGER NOT NULL,
   PRIMARY KEY(OccurrenceID),
   FOREIGN KEY(geological_contextID) REFERENCES geological_context(geological_contextID),
   FOREIGN KEY(OrganismID) REFERENCES Organism(OrganismID),
   FOREIGN KEY(EventID) REFERENCES Event(EventID)
);

CREATE TABLE identification(
   identificationID SERIAL,
   dateIdentified TIMESTAMP NOT NULL,
   identificationQualifier TEXT,
   isAcceptedIdentification BOOLEAN NOT NULL,
   TaxonID INTEGER NOT NULL,
   agentID INTEGER NOT NULL,
   OccurrenceID INTEGER,
   PRIMARY KEY(identificationID),
   FOREIGN KEY(TaxonID) REFERENCES Taxon(TaxonID),
   FOREIGN KEY(agentID) REFERENCES Agent(agentID),
   FOREIGN KEY(OccurrenceID) REFERENCES Occurrence(OccurrenceID)
);

CREATE TABLE Media(
   MediaID SERIAL,
   accessURI TEXT NOT NULL,
   associatedObservationReference VARCHAR(250) ,
   caption TEXT,
   captureDevice VARCHAR(250) ,
   comments TEXT,
   OccurrenceID INTEGER NOT NULL,
   PRIMARY KEY(MediaID),
   FOREIGN KEY(OccurrenceID) REFERENCES Occurrence(OccurrenceID)
);

CREATE TABLE measurement_or_fact(
   measurement_or_factID SERIAL,
   measurementValue VARCHAR(50) ,
   measurementAccuracy VARCHAR(50) ,
   measurementMethod VARCHAR(50) ,
   measurementDeterminedDate TIMESTAMP,
   measurementRemarks VARCHAR(50) ,
   OccurrenceID INTEGER NOT NULL,
   measurement_or_factID_1 INTEGER,
   measurementUnitID INTEGER NOT NULL,
   measurementTypeID INTEGER NOT NULL,
   PRIMARY KEY(measurement_or_factID),
   FOREIGN KEY(OccurrenceID) REFERENCES Occurrence(OccurrenceID),
   FOREIGN KEY(measurement_or_factID_1) REFERENCES measurement_or_fact(measurement_or_factID),
   FOREIGN KEY(measurementUnitID) REFERENCES measurementUnit(measurementUnitID),
   FOREIGN KEY(measurementTypeID) REFERENCES measurementType(measurementTypeID)
);

CREATE TABLE organism_interaction(
   organism_interactionID SERIAL,
   organismInteractionDescription TEXT,
   OccurrenceID INTEGER NOT NULL,
   OccurrenceID_1 INTEGER NOT NULL,
   EventID INTEGER NOT NULL,
   organismInteractionTypeID INTEGER NOT NULL,
   PRIMARY KEY(organism_interactionID),
   FOREIGN KEY(OccurrenceID) REFERENCES Occurrence(OccurrenceID),
   FOREIGN KEY(OccurrenceID_1) REFERENCES Occurrence(OccurrenceID),
   FOREIGN KEY(EventID) REFERENCES Event(EventID),
   FOREIGN KEY(organismInteractionTypeID) REFERENCES organismInteractionType(organismInteractionTypeID)
);

CREATE TABLE NucleotideAnalysis(
   NucleotideAnalysisID SERIAL,
   readCount_ BIGINT,
   totalReadCount_ BIGINT,
   identificationID INTEGER,
   NucleotideSequenceID INTEGER NOT NULL,
   MolecularProtocolID INTEGER NOT NULL,
   MaterialEntityID INTEGER NOT NULL,
   EventID INTEGER NOT NULL,
   PRIMARY KEY(NucleotideAnalysisID),
   FOREIGN KEY(identificationID) REFERENCES identification(identificationID),
   FOREIGN KEY(NucleotideSequenceID) REFERENCES NucleotideSequence(NucleotideSequenceID),
   FOREIGN KEY(MolecularProtocolID) REFERENCES MolecularProtocol(MolecularProtocolID),
   FOREIGN KEY(MaterialEntityID) REFERENCES MaterialEntity(MaterialEntityID),
   FOREIGN KEY(EventID) REFERENCES Event(EventID)
);

CREATE TABLE Occurrence_Agent(
   agentID INTEGER,
   OccurrenceID INTEGER,
   PRIMARY KEY(agentID, OccurrenceID),
   FOREIGN KEY(agentID) REFERENCES Agent(agentID),
   FOREIGN KEY(OccurrenceID) REFERENCES Occurrence(OccurrenceID)
);

CREATE TABLE Agent_User(
   agentID INTEGER,
   UtilisateurID INTEGER,
   agentRoleOrder INTEGER,
   PRIMARY KEY(agentID, UtilisateurID),
   FOREIGN KEY(agentID) REFERENCES Agent(agentID),
   FOREIGN KEY(UtilisateurID) REFERENCES Utilisateur(UtilisateurID)
);

CREATE TABLE Agent_Recherche(
   agentID INTEGER,
   rechercheID INTEGER,
   PRIMARY KEY(agentID, rechercheID),
   FOREIGN KEY(agentID) REFERENCES Agent(agentID),
   FOREIGN KEY(rechercheID) REFERENCES recherche(rechercheID)
);

CREATE TABLE occurrence_material_entity(
   OccurrenceID INTEGER,
   MaterialEntityID INTEGER,
   PRIMARY KEY(OccurrenceID, MaterialEntityID),
   FOREIGN KEY(OccurrenceID) REFERENCES Occurrence(OccurrenceID),
   FOREIGN KEY(MaterialEntityID) REFERENCES MaterialEntity(MaterialEntityID)
);

CREATE TABLE identification_reference(
   identificationID INTEGER,
   referenceID INTEGER,
   PRIMARY KEY(identificationID, referenceID),
   FOREIGN KEY(identificationID) REFERENCES identification(identificationID),
   FOREIGN KEY(referenceID) REFERENCES bibliographic_resource(referenceID)
);

CREATE TABLE event_reference(
   EventID INTEGER,
   referenceID INTEGER,
   PRIMARY KEY(EventID, referenceID),
   FOREIGN KEY(EventID) REFERENCES Event(EventID),
   FOREIGN KEY(referenceID) REFERENCES bibliographic_resource(referenceID)
);


CREATE INDEX idx_province_pays ON Province(PaysID);
CREATE INDEX idx_taxon_rank ON Taxon(taxonRankID);
CREATE INDEX idx_taxon_scientific_name ON Taxon(scientificName);
CREATE INDEX idx_taxon_genus ON Taxon(genus);
CREATE INDEX idx_taxon_family ON Taxon(family);
CREATE INDEX idx_location_province ON Location(ProvinceID);
CREATE INDEX idx_recherche_dataset ON recherche(datasetID);
CREATE INDEX idx_recherche_confidentialite ON recherche(Confidentialite);
CREATE INDEX idx_recherche_date_debut ON recherche(date_debut);
CREATE INDEX idx_recherche_date_fin ON recherche(date_fin);
CREATE INDEX idx_media_recherche_utilisateur ON Media_recherche(UtilisateurID);
CREATE INDEX idx_media_recherche_recherche ON Media_recherche(rechercheID);
CREATE INDEX idx_media_recherche_type ON Media_recherche(type_media);
CREATE INDEX idx_protocol_type ON Protocol(protocolTypeID);
CREATE INDEX idx_molecular_protocol_protocol ON MolecularProtocol(ProtocolID);
CREATE INDEX idx_material_entity_category ON MaterialEntity(materialEntityCategoryID);
CREATE INDEX idx_material_entity_type ON MaterialEntity(materialEntityTypeID);
CREATE INDEX idx_material_entity_catalog_number ON MaterialEntity(catalogNumber);
CREATE INDEX idx_material_entity_institution ON MaterialEntity(institutionCode);
CREATE INDEX idx_material_entity_collection ON MaterialEntity(collectionCode);
CREATE INDEX idx_event_protocol ON Event(ProtocolID);
CREATE INDEX idx_event_location ON Event(LocationID);
CREATE INDEX idx_event_dataset ON Event(datasetID);
CREATE INDEX idx_event_date ON Event(eventDate);
CREATE INDEX idx_occurrence_geological_context ON Occurrence(geological_contextID);
CREATE INDEX idx_occurrence_organism ON Occurrence(OrganismID);
CREATE INDEX idx_occurrence_event ON Occurrence(EventID);
CREATE INDEX idx_occurrence_status ON Occurrence(occurrenceStatus);
CREATE INDEX idx_occurrence_basis ON Occurrence(basisOfRecord);
CREATE INDEX idx_identification_taxon ON identification(TaxonID);
CREATE INDEX idx_identification_agent ON identification(agentID);
CREATE INDEX idx_identification_occurrence ON identification(OccurrenceID);
CREATE INDEX idx_identification_date ON identification(dateIdentified);
CREATE INDEX idx_identification_accepted ON identification(isAcceptedIdentification);
CREATE INDEX idx_media_occurrence ON Media(OccurrenceID);
CREATE INDEX idx_measurement_occurrence ON measurement_or_fact(OccurrenceID);
CREATE INDEX idx_measurement_unit ON measurement_or_fact(measurementUnitID);
CREATE INDEX idx_measurement_type ON measurement_or_fact(measurementTypeID);
CREATE INDEX idx_measurement_parent ON measurement_or_fact(measurement_or_factID_1);
CREATE INDEX idx_measurement_date ON measurement_or_fact(measurementDeterminedDate);
CREATE INDEX idx_interaction_occurrence ON organism_interaction(OccurrenceID);
CREATE INDEX idx_interaction_occurrence_1 ON organism_interaction(OccurrenceID_1);
CREATE INDEX idx_interaction_event ON organism_interaction(EventID);
CREATE INDEX idx_interaction_type ON organism_interaction(organismInteractionTypeID);
CREATE INDEX idx_nucleotide_analysis_identification ON NucleotideAnalysis(identificationID);
CREATE INDEX idx_nucleotide_analysis_sequence ON NucleotideAnalysis(NucleotideSequenceID);
CREATE INDEX idx_nucleotide_analysis_protocol ON NucleotideAnalysis(MolecularProtocolID);
CREATE INDEX idx_nucleotide_analysis_material ON NucleotideAnalysis(MaterialEntityID);
CREATE INDEX idx_nucleotide_analysis_event ON NucleotideAnalysis(EventID);
CREATE INDEX idx_occurrence_agent_occurrence ON Occurrence_Agent(OccurrenceID);
CREATE INDEX idx_agent_user_utilisateur ON Agent_User(UtilisateurID);
CREATE INDEX idx_agent_recherche_recherche ON Agent_Recherche(rechercheID);
CREATE INDEX idx_occurrence_material_entity_material ON occurrence_material_entity(MaterialEntityID);
CREATE INDEX idx_identification_reference_reference ON identification_reference(referenceID);
CREATE INDEX idx_event_reference_reference ON event_reference(referenceID);