CREATE TABLE Dataset(
   datasetID SERIAL,
   datasetName VARCHAR(500)  NOT NULL,
   Licence TEXT,
   rightsHolder TEXT,
   PRIMARY KEY(datasetID)
);

CREATE TABLE AgentType(
   AgentTypeID SERIAL,
   type VARCHAR(100)  NOT NULL,
   agentRoleOrder VARCHAR(50)  NOT NULL,
   PRIMARY KEY(AgentTypeID)
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

CREATE TABLE basisOfRecord(
   Id_basisOfRecord SERIAL,
   record VARCHAR(250)  NOT NULL,
   PRIMARY KEY(Id_basisOfRecord),
   UNIQUE(record)
);

CREATE TABLE occurrenceStatus(
   occurrenceStatusID SERIAL,
   status VARCHAR(50)  NOT NULL,
   PRIMARY KEY(occurrenceStatusID),
   UNIQUE(status)
);

CREATE TABLE taxonRank(
   taxonRankID SERIAL,
   taxonRank VARCHAR(250)  NOT NULL,
   PRIMARY KEY(taxonRankID)
);

CREATE TABLE organismScope(
   organismScopeID SERIAL,
   organismScope VARCHAR(250)  NOT NULL,
   PRIMARY KEY(organismScopeID),
   UNIQUE(organismScope)
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

CREATE TABLE Confidentialite(
   ConfidentialiteID SERIAL,
   status VARCHAR(50)  NOT NULL,
   PRIMARY KEY(ConfidentialiteID),
   UNIQUE(status)
);

CREATE TABLE type_meda(
   type_mediaID SERIAL,
   type VARCHAR(50) ,
   PRIMARY KEY(type_mediaID)
);

CREATE TABLE role(
   roleID SERIAL,
   role VARCHAR(50) ,
   PRIMARY KEY(roleID)
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
   organismScopeID INTEGER NOT NULL,
   PRIMARY KEY(OrganismID),
   FOREIGN KEY(organismScopeID) REFERENCES organismScope(organismScopeID)
);

CREATE TABLE Utilisateur(
   UtilisateurID SERIAL,
   nom VARCHAR(250)  NOT NULL,
   prenom VARCHAR(250) ,
   telephone VARCHAR(50) ,
   email VARCHAR(50)  NOT NULL,
   mot_de_passe VARCHAR(500)  NOT NULL,
   roleID INTEGER NOT NULL,
   PRIMARY KEY(UtilisateurID),
   UNIQUE(email),
   FOREIGN KEY(roleID) REFERENCES role(roleID)
);

CREATE TABLE recherche(
   rechercheID SERIAL,
   sujet VARCHAR(250) ,
   description TEXT,
   date_debut DATE NOT NULL,
   date_creation TIMESTAMP NOT NULL,
   date_fin DATE NOT NULL,
   Note_perso VARCHAR(50) ,
   datasetID INTEGER NOT NULL,
   ConfidentialiteID INTEGER NOT NULL,
   PRIMARY KEY(rechercheID),
   FOREIGN KEY(datasetID) REFERENCES Dataset(datasetID),
   FOREIGN KEY(ConfidentialiteID) REFERENCES Confidentialite(ConfidentialiteID)
);

CREATE TABLE Media_recherche(
   Media_rechercheID SERIAL,
   accessURI TEXT,
   caption TEXT,
   comments VARCHAR(50) ,
   UtilisateurID INTEGER NOT NULL,
   rechercheID INTEGER NOT NULL,
   type_mediaID INTEGER NOT NULL,
   PRIMARY KEY(Media_rechercheID),
   FOREIGN KEY(UtilisateurID) REFERENCES Utilisateur(UtilisateurID),
   FOREIGN KEY(rechercheID) REFERENCES recherche(rechercheID),
   FOREIGN KEY(type_mediaID) REFERENCES type_meda(type_mediaID)
);

CREATE TABLE Agent(
   agentID SERIAL,
   agentType VARCHAR(250)  NOT NULL,
   agentRemarks TEXT,
   UtilisateurID INTEGER NOT NULL,
   AgentTypeID INTEGER NOT NULL,
   PRIMARY KEY(agentID),
   FOREIGN KEY(UtilisateurID) REFERENCES Utilisateur(UtilisateurID),
   FOREIGN KEY(AgentTypeID) REFERENCES AgentType(AgentTypeID)
);

CREATE TABLE Event(
   EventID SERIAL,
   eventDate TIMESTAMP,
   samplingProtocol TEXT,
   LocationID INTEGER NOT NULL,
   datasetID INTEGER NOT NULL,
   PRIMARY KEY(EventID),
   FOREIGN KEY(LocationID) REFERENCES Location(LocationID),
   FOREIGN KEY(datasetID) REFERENCES Dataset(datasetID)
);

CREATE TABLE Occurrence(
   OccurrenceID SERIAL,
   individualCount INTEGER,
   geological_contextID INTEGER NOT NULL,
   OrganismID INTEGER NOT NULL,
   occurrenceStatusID INTEGER NOT NULL,
   Id_basisOfRecord INTEGER NOT NULL,
   EventID INTEGER NOT NULL,
   PRIMARY KEY(OccurrenceID),
   FOREIGN KEY(geological_contextID) REFERENCES geological_context(geological_contextID),
   FOREIGN KEY(OrganismID) REFERENCES Organism(OrganismID),
   FOREIGN KEY(occurrenceStatusID) REFERENCES occurrenceStatus(occurrenceStatusID),
   FOREIGN KEY(Id_basisOfRecord) REFERENCES basisOfRecord(Id_basisOfRecord),
   FOREIGN KEY(EventID) REFERENCES Event(EventID)
);

CREATE TABLE identification(
   identificationID SERIAL,
   dateIdentified TIMESTAMP NOT NULL,
   identificationQualifier TEXT,
   isAcceptedIdentification BOOLEAN NOT NULL,
   TaxonID INTEGER NOT NULL,
   agentID INTEGER NOT NULL,
   OccurrenceID INTEGER NOT NULL,
   PRIMARY KEY(identificationID),
   FOREIGN KEY(TaxonID) REFERENCES Taxon(TaxonID),
   FOREIGN KEY(agentID) REFERENCES Agent(agentID),
   FOREIGN KEY(OccurrenceID) REFERENCES Occurrence(OccurrenceID)
);

CREATE TABLE Media(
   Id_Media SERIAL,
   accessURI TEXT NOT NULL,
   associatedObservationReference VARCHAR(50) ,
   caption VARCHAR(50) ,
   captureDevice VARCHAR(50) ,
   comments VARCHAR(50) ,
   OccurrenceID INTEGER NOT NULL,
   PRIMARY KEY(Id_Media),
   FOREIGN KEY(OccurrenceID) REFERENCES Occurrence(OccurrenceID)
);

CREATE TABLE material_sample(
   material_sampleID SERIAL,
   Note VARCHAR(50) ,
   OccurrenceID INTEGER NOT NULL,
   PRIMARY KEY(material_sampleID),
   FOREIGN KEY(OccurrenceID) REFERENCES Occurrence(OccurrenceID)
);

CREATE TABLE measurement_or_fact(
   measurement_or_factID SERIAL,
   measurementValue VARCHAR(50) ,
   measurementAccuracy VARCHAR(50) ,
   measurementMethod VARCHAR(50) ,
   measurementDeterminedDate TIMESTAMP,
   measurementRemarks VARCHAR(50) ,
   parentMeasurementID INTEGER,
   OccurrenceID INTEGER NOT NULL,
   measurement_or_factID_1 INTEGER NOT NULL,
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

CREATE TABLE OccurenceAgent(
   agentID INTEGER,
   OccurrenceID INTEGER,
   PRIMARY KEY(agentID, OccurrenceID),
   FOREIGN KEY(agentID) REFERENCES Agent(agentID),
   FOREIGN KEY(OccurrenceID) REFERENCES Occurrence(OccurrenceID)
);

CREATE TABLE Agent_Recherche(
   agentID INTEGER,
   rechercheID INTEGER,
   PRIMARY KEY(agentID, rechercheID),
   FOREIGN KEY(agentID) REFERENCES Agent(agentID),
   FOREIGN KEY(rechercheID) REFERENCES recherche(rechercheID)
);
