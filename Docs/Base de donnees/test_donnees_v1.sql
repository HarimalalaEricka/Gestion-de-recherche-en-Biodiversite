-- scriptv4.sql

-- Donnees de test Nexora (PostgreSQL)
-- A executer apres le script de creation du schema.
-- Les donnees sont fictives et couvrent les relations principales.
BEGIN;

INSERT INTO recherche (rechercheID,sujet,description,date_debut,date_creation,date_fin,Note_perso,Confidentialite) VALUES
(1,'Inventaire des oiseaux de la foret humide','Observation pilote des oiseaux dans la region d''Andasibe.','2026-01-10','2026-01-08 09:00:00',NULL,'Prevoir une sortie en saison humide.', 'PRIVATE'),
(2,'Lemuriens et fragmentation forestiere','etude des observations de lemuriens dans plusieurs sites.','2025-06-01','2025-05-20 10:30:00','2025-12-15','Comparer les sites fragmentes et continus.', 'PUBLIC');

INSERT INTO Dataset (datasetID,datasetName,Licence,rightsHolder,rechercheID) VALUES
(1,'Observations oiseaux Andasibe','CC BY 4.0','equipe Nexora - donnees fictives',1),
(2,'Observations lemuriens 2025','CC BY-NC 4.0','equipe Nexora - donnees fictives',2);

INSERT INTO Pays (PaysID,pays) VALUES (1,'Madagascar'),(2,'France');
INSERT INTO Province (ProvinceID,province,PaysID) VALUES (1,'Alaotra-Mangoro',1),(2,'Analamanga',1),(3,'Occitanie',2);
INSERT INTO taxonRank (taxonRankID,taxonRank) VALUES (1,'species'),(2,'genus'),(3,'family');
INSERT INTO measurementType (measurementTypeID,measurementType) VALUES (1,'temperature'),(2,'body length'),(3,'group size');
INSERT INTO measurementUnit (measurementUnitID,measurementUnit) VALUES (1,'°C'),(2,'cm'),(3,'individuals');
INSERT INTO geological_context (geological_contextID,group_,formation,member_,bed,earliestPeriodOrLowestSystem,latestPeriodOrHighestSystem,earliestAgeOrLowestStage,latestAgeOrHighestStage)
VALUES (1,'Quaternary','Alluvions recentes','Non precise','Sol forestier','Quaternary','Quaternary','Holocene','Holocene');
INSERT INTO organismInteractionType (organismInteractionTypeID,type) VALUES (1,'predation'),(2,'pollination'),(3,'competition');
INSERT INTO protocolType (protocolTypeID,type) VALUES (1,'FIELD_OBSERVATION'),(2,'MOLECULAR');
INSERT INTO NucleotideSequence (NucleotideSequenceID,sequence_,nucleotideSequenceRemarks_) VALUES
(1,'ACGTACGTACGT','Sequence fictive pour test uniquement.'),
(2,'TTGCAATTGCAA','Sequence fictive pour test uniquement.');
INSERT INTO materialEntityType (materialEntityTypeID,type) VALUES (1,'Specimen'),(2,'Tissue sample');
INSERT INTO materialEntityCategory (materialEntityCategoryID,category) VALUES (1,'Preserved specimen'),(2,'Molecular sample');
INSERT INTO Agent (agentID,agentRemarks,agentType) VALUES
(1,'Chercheuse responsable fictive','PERSON'),(2,'equipe de terrain fictive','GROUP'),(3,'Centre de recherche fictif','INSTITUTION'),(4,'Agent de saisie fictif','PERSON');
INSERT INTO Location (LocationID,municipality,locality,decimalLatitude,decimalLongitude,geodeticDatum,ProvinceID) VALUES
(1,'Andasibe','Foret pres du parc',-18.9360,48.4130,'WGS84',1),
(2,'Moramanga','Lisiere forestiere',-18.9470,48.2300,'WGS84',1),
(3,'Antananarivo','Jardin botanique',-18.8792,47.5079,'WGS84',2);
INSERT INTO Taxon (TaxonID,scientificName,kingdom,phylum,class,order_,family,genus,specificEpithet,taxonRankID) VALUES
(1,'Varecia variegata','Animalia','Chordata','Mammalia','Primates','Lemuridae','Varecia','variegata',1),
(2,'Indri indri','Animalia','Chordata','Mammalia','Primates','Indriidae','Indri','indri',1),
(3,'Euryceros prevostii','Animalia','Chordata','Aves','Passeriformes','Eurycerotidae','Euryceros','prevostii',1);
INSERT INTO Organism (OrganismID,organismName,causeOfDeath,organismRemarks,organismScope) VALUES
(1,'Lemurien observe',NULL,'Individu observe a distance.','INDIVIDUAL'),
(2,'Groupe d''indri',NULL,'Groupe entendu puis observe.','GROUP'),
(3,'Oiseau observe',NULL,'Observation visuelle.','INDIVIDUAL');
INSERT INTO Utilisateur (UtilisateurID,nom,prenom,telephone,email,mot_de_passe,role,statut,date_creation) VALUES
(1,'Ravelomanana','Miora','0340000001','miora.test@nexora.example','$2b$12$FAUX_HASH_RESERVE_AU_TEST','RESEARCHER','ACTIVE','2026-01-08 08:00:00'),
(2,'Rakoto','Tiana','0340000002','tiana.test@nexora.example','$2b$12$FAUX_HASH_RESERVE_AU_TEST','DATA_ENTRY','ACTIVE','2026-01-09 08:00:00'),
(3,'Andrianina','Lova','0340000003','lova.test@nexora.example','$2b$12$FAUX_HASH_RESERVE_AU_TEST','VIEWER','INVITE','2026-01-10 08:00:00'),
(4,'Admin','Nexora','0340000004','admin.test@nexora.example','$2b$12$FAUX_HASH_RESERVE_AU_TEST','ADMIN','ACTIVE','2026-01-05 08:00:00');
INSERT INTO Media_recherche (Media_rechercheID,accessURI,caption,comments,UtilisateurID,rechercheID,type_media) VALUES
(1,'https://example.org/nexora/test-site.jpg','Site de terrain (fictif)','URL illustrative, aucun vrai fichier.',1,1,'IMAGE'),
(2,'https://example.org/nexora/protocole.pdf','Protocole de terrain (fictif)','Document de demonstration.',1,2,'DOCUMENT');
INSERT INTO Protocol (ProtocolID,protocolName,protocolDescription,protocolReferences,protocolRemarks,protocolTypeID) VALUES
(1,'Point count','Comptage visuel et auditif pendant une duree fixe.','Protocole de demonstration Nexora','Standardiser la duree des observations.',1),
(2,'ADN environnemental','Collecte et analyse moleculaire fictive.','Reference de demonstration Nexora','echantillon fictif.',2);
INSERT INTO MolecularProtocol (MolecularProtocolID,assayType,molecularProtocolRemarks,ProtocolID) VALUES (1,'DNA metabarcoding','Protocole de test uniquement.',2);
INSERT INTO MaterialEntity (MaterialEntityID,institutionCode,collectionCode,catalogNumber,preparations,disposition,materialEntityRemarks,materialEntityCategoryID,materialEntityTypeID) VALUES
(1,'NEXORA','ORN','NEX-ORN-0001','Photographie numerique','Disponible','Materiel fictif de test.',1,1),
(2,'NEXORA','DNA','NEX-DNA-0001','Aliquote fictive','Stockage simule','echantillon fictif, non biologique.',2,2);
INSERT INTO bibliographic_resource (referenceID,referenceRemarks,reference,referenceType) VALUES
(1,'Reference illustrative a remplacer par une vraie source.','Reference de demonstration Nexora, 2026.','REPORT'),
(2,'URL illustrative pour tester les liens bibliographiques.','https://example.org/nexora/biodiversite','WEBSITE');
INSERT INTO Event (EventID,eventDate,samplingProtocol,ProtocolID,LocationID,datasetID) VALUES
(1,'2026-02-10 06:30:00','Point count pendant 10 minutes',1,1,1),
(2,'2026-02-10 08:15:00','Observation opportuniste',1,2,1),
(3,'2025-08-20 07:00:00','Observation visuelle et auditive',1,1,2),
(4,'2026-02-11 09:00:00','Collecte moleculaire fictive',2,3,1);
INSERT INTO Occurrence (OccurrenceID,individualCount,geological_contextID,OrganismID,occurrenceStatus,basisOfRecord,EventID) VALUES
(1,1,NULL,3,'DETECTED','HUMAN_OBSERVATION',1),
(2,3,NULL,2,'DETECTED','HUMAN_OBSERVATION',2),
(3,2,1,1,'DETECTED','HUMAN_OBSERVATION',3),
(4,0,NULL,1,'NOT_DETECTED','HUMAN_OBSERVATION',4);
INSERT INTO identification (identificationID,dateIdentified,identificationQualifier,isAcceptedIdentification,TaxonID,agentID,OccurrenceID) VALUES
(1,'2026-02-10 07:00:00','Identification visuelle',TRUE,3,1,1),
(2,'2026-02-10 08:45:00','Identification confirmee',TRUE,2,1,2),
(3,'2025-08-20 07:30:00','Identification probable',TRUE,1,2,3),
(4,'2026-02-11 09:30:00','Resultat de test fictif',TRUE,1,1,4);
INSERT INTO Media (MediaID,accessURI,associatedObservationReference,caption,captureDevice,comments,OccurrenceID) VALUES
(1,'https://example.org/nexora/occurrence-1.jpg','OBS-0001','Oiseau observe (image fictive)','Appareil photo','Lien de demonstration.',1),
(2,'https://example.org/nexora/occurrence-2.jpg','OBS-0002','Indri observe (image fictive)','Telephone','Lien de demonstration.',2),
(3,'https://example.org/nexora/occurrence-3.mp3','OBS-0003','Son d''indri (audio fictif)','Enregistreur','Lien de demonstration.',3);
INSERT INTO measurement_or_fact (measurement_or_factID,measurementValue,measurementAccuracy,measurementMethod,measurementDeterminedDate,measurementRemarks,OccurrenceID,measurement_or_factID_1,measurementUnitID,measurementTypeID) VALUES
(1,'24.5','0.5','Thermometre','2026-02-10 06:35:00','Temperature ambiante.',1,NULL,1,1),
(2,'68','1','Estimation visuelle','2026-02-10 08:50:00','Longueur estimee en cm.',2,NULL,2,2),
(3,'3','0','Comptage direct','2026-02-10 08:20:00','Taille du groupe.',2,NULL,3,3),
(4,'25.0','0.5','Thermometre','2026-02-10 06:40:00','Mesure liee a la precedente.',1,1,1,1);
INSERT INTO organism_interaction (organism_interactionID,organismInteractionDescription,OccurrenceID,OccurrenceID_1,EventID,organismInteractionTypeID) VALUES
(1,'Interaction fictive observee entre deux occurrences.',1,2,2,3);
INSERT INTO NucleotideAnalysis (NucleotideAnalysisID,readCount_,totalReadCount_,identificationID,NucleotideSequenceID,MolecularProtocolID,MaterialEntityID,EventID) VALUES
(1,1250,2000,4,1,1,2,4),
(2,800,2000,NULL,2,1,2,4);
INSERT INTO Occurrence_Agent (agentID,OccurrenceID) VALUES (1,1),(2,2),(2,3),(4,4);
INSERT INTO Agent_User (agentID,UtilisateurID,agentRoleOrder) VALUES (1,1,1),(2,2,1),(3,4,1),(4,2,2);
INSERT INTO Agent_Recherche (agentID,rechercheID) VALUES (1,1),(2,1),(2,2),(3,2);
INSERT INTO occurrence_material_entity (OccurrenceID,MaterialEntityID) VALUES (1,1),(4,2);
INSERT INTO identification_reference (identificationID,referenceID) VALUES (1,1),(2,2),(3,1);
INSERT INTO event_reference (EventID,referenceID) VALUES (1,1),(3,2),(4,1);
INSERT INTO Attribut (AttributID,nom,cle,type_donnee) VALUES
(1,'Temperature','temperature','number'),(2,'Meteo','weather','text'),(3,'Presence confirmee','confirmed_presence','boolean'),(4,'Date de visite','visit_date','date');
INSERT INTO tableau_personnalise (tableau_personnaliseID,nom,datasetID,agentID) VALUES
(1,'Conditions de terrain',1,2),(2,'Suivi des sites',2,1);
INSERT INTO ligne (ligneID,date_creation,tableau_personnaliseID,agentID) VALUES
(1,'2026-02-10',1,2),(2,'2026-02-11',1,2),(3,'2025-08-20',2,1);
INSERT INTO valeur (valeurID,valeur_texte,valeur_numeric,valeur_boolean,valeur_date,ligneID,AttributID) VALUES
(1,NULL,24.50,NULL,NULL,1,1),(2,'Nuageux',NULL,NULL,NULL,1,2),(3,NULL,NULL,TRUE,NULL,1,3),(4,NULL,NULL,NULL,'2026-02-10 00:00:00',1,4),
(5,NULL,25.00,NULL,NULL,2,1),(6,'Ensoleille',NULL,NULL,NULL,2,2),(7,NULL,NULL,FALSE,NULL,3,3);
INSERT INTO notification (notificationID,type,titre,lue,created_at,read_at,message,lien,UtilisateurID) VALUES
(1,'invitation','Invitation a rejoindre Nexora',FALSE,'2026-02-10 08:00:00',NULL,'Vous avez ete invite a rejoindre un espace de recherche.','/invitations/1',3),
(2,'validation_demandee','Donnee a valider',FALSE,'2026-02-10 09:00:00',NULL,'Une donnee saisie attend votre validation.','/occurrences/4',1),
(3,'donnee_validee','Donnee validee',TRUE,'2026-02-09 16:00:00','2026-02-09 16:30:00','Une observation a ete validee.','/occurrences/1',2),
(4,'partage_recu','Recherche partagee',FALSE,'2026-02-11 10:00:00',NULL,'Une recherche publique est disponible.','/recherches/2',3);

-- Met a jour les sequences SERIAL apres l'insertion d'identifiants explicites.
SELECT setval(pg_get_serial_sequence('recherche','rechercheid'), COALESCE((SELECT MAX(rechercheID) FROM recherche),1));
SELECT setval(pg_get_serial_sequence('dataset','datasetid'), COALESCE((SELECT MAX(datasetID) FROM Dataset),1));
SELECT setval(pg_get_serial_sequence('pays','paysid'), COALESCE((SELECT MAX(PaysID) FROM Pays),1));
SELECT setval(pg_get_serial_sequence('province','provinceid'), COALESCE((SELECT MAX(ProvinceID) FROM Province),1));
SELECT setval(pg_get_serial_sequence('taxonrank','taxonrankid'), COALESCE((SELECT MAX(taxonRankID) FROM taxonRank),1));
SELECT setval(pg_get_serial_sequence('measurementtype','measurementtypeid'), COALESCE((SELECT MAX(measurementTypeID) FROM measurementType),1));
SELECT setval(pg_get_serial_sequence('measurementunit','measurementunitid'), COALESCE((SELECT MAX(measurementUnitID) FROM measurementUnit),1));
SELECT setval(pg_get_serial_sequence('geological_context','geological_contextid'), COALESCE((SELECT MAX(geological_contextID) FROM geological_context),1));
SELECT setval(pg_get_serial_sequence('organisminteractiontype','organisminteractiontypeid'), COALESCE((SELECT MAX(organismInteractionTypeID) FROM organismInteractionType),1));
SELECT setval(pg_get_serial_sequence('protocoltype','protocoltypeid'), COALESCE((SELECT MAX(protocolTypeID) FROM protocolType),1));
SELECT setval(pg_get_serial_sequence('nucleotidesequence','nucleotidesequenceid'), COALESCE((SELECT MAX(NucleotideSequenceID) FROM NucleotideSequence),1));
SELECT setval(pg_get_serial_sequence('materialentitytype','materialentitytypeid'), COALESCE((SELECT MAX(materialEntityTypeID) FROM materialEntityType),1));
SELECT setval(pg_get_serial_sequence('materialentitycategory','materialentitycategoryid'), COALESCE((SELECT MAX(materialEntityCategoryID) FROM materialEntityCategory),1));
SELECT setval(pg_get_serial_sequence('agent','agentid'), COALESCE((SELECT MAX(agentID) FROM Agent),1));
SELECT setval(pg_get_serial_sequence('location','locationid'), COALESCE((SELECT MAX(LocationID) FROM Location),1));
SELECT setval(pg_get_serial_sequence('taxon','taxonid'), COALESCE((SELECT MAX(TaxonID) FROM Taxon),1));
SELECT setval(pg_get_serial_sequence('organism','organismid'), COALESCE((SELECT MAX(OrganismID) FROM Organism),1));
SELECT setval(pg_get_serial_sequence('utilisateur','utilisateurid'), COALESCE((SELECT MAX(UtilisateurID) FROM Utilisateur),1));
SELECT setval(pg_get_serial_sequence('media_recherche','media_rechercheid'), COALESCE((SELECT MAX(Media_rechercheID) FROM Media_recherche),1));
SELECT setval(pg_get_serial_sequence('protocol','protocolid'), COALESCE((SELECT MAX(ProtocolID) FROM Protocol),1));
SELECT setval(pg_get_serial_sequence('molecularprotocol','molecularprotocolid'), COALESCE((SELECT MAX(MolecularProtocolID) FROM MolecularProtocol),1));
SELECT setval(pg_get_serial_sequence('materialentity','materialentityid'), COALESCE((SELECT MAX(MaterialEntityID) FROM MaterialEntity),1));
SELECT setval(pg_get_serial_sequence('bibliographic_resource','referenceid'), COALESCE((SELECT MAX(referenceID) FROM bibliographic_resource),1));
SELECT setval(pg_get_serial_sequence('event','eventid'), COALESCE((SELECT MAX(EventID) FROM Event),1));
SELECT setval(pg_get_serial_sequence('occurrence','occurrenceid'), COALESCE((SELECT MAX(OccurrenceID) FROM Occurrence),1));
SELECT setval(pg_get_serial_sequence('identification','identificationid'), COALESCE((SELECT MAX(identificationID) FROM identification),1));
SELECT setval(pg_get_serial_sequence('media','mediaid'), COALESCE((SELECT MAX(MediaID) FROM Media),1));
SELECT setval(pg_get_serial_sequence('measurement_or_fact','measurement_or_factid'), COALESCE((SELECT MAX(measurement_or_factID) FROM measurement_or_fact),1));
SELECT setval(pg_get_serial_sequence('organism_interaction','organism_interactionid'), COALESCE((SELECT MAX(organism_interactionID) FROM organism_interaction),1));
SELECT setval(pg_get_serial_sequence('nucleotideanalysis','nucleotideanalysisid'), COALESCE((SELECT MAX(NucleotideAnalysisID) FROM NucleotideAnalysis),1));
SELECT setval(pg_get_serial_sequence('attribut','attributid'), COALESCE((SELECT MAX(AttributID) FROM Attribut),1));
SELECT setval(pg_get_serial_sequence('tableau_personnalise','tableau_personnaliseid'), COALESCE((SELECT MAX(tableau_personnaliseID) FROM tableau_personnalise),1));
SELECT setval(pg_get_serial_sequence('ligne','ligneid'), COALESCE((SELECT MAX(ligneID) FROM ligne),1));
SELECT setval(pg_get_serial_sequence('valeur','valeurid'), COALESCE((SELECT MAX(valeurID) FROM valeur),1));
SELECT setval(pg_get_serial_sequence('notification','notificationid'), COALESCE((SELECT MAX(notificationID) FROM notification),1));

COMMIT;
