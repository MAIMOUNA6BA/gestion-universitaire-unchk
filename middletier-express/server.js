const express = require('express');
const cors = require('cors');
const multer = require('multer');
const path = require('path');
const fs = require('fs');

const app = express();

// Configuration des CORS pour autoriser Angular
app.use(cors({
    origin: 'http://localhost:4200',
    methods: ['GET', 'POST'],
    allowedHeaders: ['Content-Type']
}));

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// S'assurer que le dossier 'uploads' existe sur le disque pour les logos/fichiers
const uploadDir = path.join(__dirname, 'uploads');
if (!fs.existsSync(uploadDir)) {
    fs.mkdirSync(uploadDir, { recursive: true });
}
// Rendre le dossier des images/fichiers accessible publiquement via HTTP
app.use('/uploads', express.static(uploadDir));

// Configuration du stockage des images avec Multer
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, uploadDir);
    },
    filename: (req, file, cb) => {
        // Génère un nom unique pour éviter les doublons (ex: 1716241234567-logo.jpg)
        const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1E9);
        cb(null, uniqueSuffix + path.extname(file.originalname));
    }
});
const upload = multer({ storage: storage });

// --- DONNÉES TEMPORAIRES (FORMATIONS) ---
let formations = [
    { id: 1, nom: "Ingénieur Logiciel", logoUrl: "http://localhost:5000/uploads/default.png" },
    { id: 2, nom: "Communication Digitale", logoUrl: "http://localhost:5000/uploads/default.png" },
    { id: 3, nom: "Informatique", logoUrl: "http://localhost:5000/uploads/default.png" }
];

// =========================================================================
// 1. Route GET : Récupérer les formations pour remplir les listes déroulantes
// =========================================================================
app.get('/api/formations', (req, res) => {
    res.json(formations);
});

// =========================================================================
// 2. Route POST : Enregistrer une filière/formation avec son logo officiel
// =========================================================================
app.post('/api/formations', upload.single('logo'), (req, res) => {
    try {
        if (!req.body.nom) {
            return res.status(400).json({ error: "Le nom de la filière est requis." });
        }

        // Si une image a été jointe, on génère son URL, sinon on applique une image par défaut
        const logoUrl = req.file
            ? `http://localhost:5000/uploads/${req.file.filename}`
            : "http://localhost:5000/uploads/default.png";

        const nouvelleFormation = {
            id: formations.length + 1,
            nom: req.body.nom,
            logoUrl: logoUrl
        };

        formations.push(nouvelleFormation);
        console.log("📁 Nouvelle formation ajoutée :", nouvelleFormation);
        res.status(201).json(nouvelleFormation);
    } catch (err) {
        console.error("❌ Erreur lors du traitement du logo :", err);
        res.status(500).json({ error: "Erreur interne lors du téléversement." });
    }
});

// =========================================================================
// 3. Route POST : Transmettre l'étudiant d'Angular (Petit OU Grand Formulaire) vers Spring Boot
// =========================================================================
app.post('/api/etudiants', async (req, res) => {
    console.log("📥 Requête d'inscription reçue d'Angular :", req.body);
    try {
        // Express centralise et harmonise tous les champs reçus d'Angular
        const etudiantData = {
            ine: req.body.ine || req.body.idEtudiant,
            nom: req.body.nom,
            prenom: req.body.prenom,
            formation: req.body.formation || req.body.formationPrincipale,

            // Propriétés complémentaires du grand formulaire
            dateNaissance: req.body.dateNaissance || null,
            promo: req.body.promo || null,
            anneeDebut: req.body.anneeDebut || null,
            anneeSortie: req.body.anneeSortie || null,
            diplomes: req.body.diplomes || null,

            // Accepte 'certifications' ou 'autresFormations' pour éviter le rejet de Spring Boot
            certifications: req.body.certifications || req.body.autresFormations || null
        };

        // Envoi des données au backend Java Spring Boot
        const response = await fetch('http://localhost:8080/api/etudiants', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(etudiantData)
        });

        if (response.ok) {
            const data = await response.json();
            console.log("✅ Étudiant enregistré avec succès dans Spring Boot !");
            res.status(201).json(data);
        } else {
            const errorText = await response.text();
            console.error(`❌ Rejet de Spring Boot (Code ${response.status}) :`, errorText);
            res.status(response.status).json({ error: "Erreur de validation ou doublon côté serveur Spring Boot." });
        }
    } catch (error) {
        console.error("🚨 Connexion impossible avec Spring Boot sur le port 8080 :", error.message);
        res.status(500).json({ error: "Le serveur de données Spring Boot (Java) est injoignable." });
    }
});

// =========================================================================
// 4. Route GET : Récupérer les étudiants de Spring Boot pour rafraîchir le tableau Angular
// =========================================================================
app.get('/api/etudiants', async (req, res) => {
    console.log("📤 Demande de récupération de la liste des étudiants...");
    try {
        const response = await fetch('http://localhost:8080/api/etudiants', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });

        if (response.ok) {
            const data = await response.json();
            console.log(`✅ ${data.length} étudiant(s) récupéré(s) depuis Spring Boot.`);
            res.status(200).json(data);
        } else {
            console.error(`❌ Impossible de charger les étudiants (Code ${response.status})`);
            res.status(response.status).json({ error: "Erreur lors de la récupération des données de Spring Boot." });
        }
    } catch (error) {
        console.error("🚨 Connexion impossible avec Spring Boot lors du chargement :", error.message);
        res.status(500).json({ error: "Le serveur Java Spring Boot est hors ligne ou injoignable." });
    }
});

// =========================================================================
// 5. AJOUT : Route GET pour récupérer la liste des formateurs depuis Spring Boot
// =========================================================================
app.get('/api/formateurs', async (req, res) => {
    console.log("📤 Demande de récupération de la liste des formateurs...");
    try {
        const response = await fetch('http://localhost:8080/api/formateurs', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });

        if (response.ok) {
            const data = await response.json();
            console.log(`✅ ${data.length} formateur(s) récupéré(s) depuis Spring Boot.`);
            res.status(200).json(data);
        } else {
            console.error(`❌ Impossible de charger les formateurs (Code ${response.status})`);
            res.status(response.status).json({ error: "Erreur lors de la récupération des formateurs de Spring Boot." });
        }
    } catch (error) {
        console.error("🚨 Connexion impossible avec Spring Boot lors du chargement des formateurs :", error.message);
        res.status(500).json({ error: "Le serveur Java Spring Boot est hors ligne ou injoignable." });
    }
});

// =========================================================================
// 6. AJOUT : Route POST pour envoyer un nouveau formateur vers Spring Boot
// =========================================================================
app.post('/api/formateurs', async (req, res) => {
    console.log("📥 Requête d'ajout de formateur reçue d'Angular :", req.body);
    try {
        const response = await fetch('http://localhost:8080/api/formateurs', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(req.body)
        });

        if (response.ok) {
            const data = await response.json();
            console.log("✅ Formateur enregistré avec succès dans Spring Boot !");
            res.status(201).json(data);
        } else {
            const errorText = await response.text();
            console.error(`❌ Rejet de Spring Boot (Code ${response.status}) :`, errorText);
            res.status(response.status).json({ error: "Erreur de validation ou doublon côté serveur Spring Boot." });
        }
    } catch (error) {
        console.error("🚨 Connexion impossible avec Spring Boot sur le port 8080 :", error.message);
        res.status(500).json({ error: "Le serveur de données Spring Boot (Java) est injoignable." });
    }
});

// Lancement du serveur Express intermédiaire
app.listen(5000, () => {
    console.log("====================================================");
    console.log("🚀 SERVEUR EXPRESS INTERMÉDIAIRE ACTIF SUR LE PORT : 5000");
    console.log("====================================================");
});