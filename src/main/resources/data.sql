-- Creazione sequenza per la tabella utente
ALTER TABLE utente ALTER COLUMN id SET DEFAULT nextval('utente_id_seq');
ALTER TABLE prodotto ALTER COLUMN id SET DEFAULT nextval('prodotto_id_seq');
ALTER TABLE commento ALTER COLUMN id SET DEFAULT nextval('commento_id_seq');

-- Aggiunta vincolo di unicità per il nome del prodotto
ALTER TABLE prodotto ADD CONSTRAINT unique_nome UNIQUE (nome);

-- Query generica per reimpostare il valore massimo della sequenza automatica degli id
/* SELECT setval('nome_tabella_id_seq', (SELECT MAX(id) FROM nome_tabella)); */

-- Svuota le tabelle esistenti e resetta le sequenze
TRUNCATE TABLE prodotti_simili RESTART IDENTITY CASCADE;
TRUNCATE TABLE commento RESTART IDENTITY CASCADE;
TRUNCATE TABLE prodotto RESTART IDENTITY CASCADE;
TRUNCATE TABLE utente RESTART IDENTITY CASCADE;

SELECT setval('utente_id_seq', (SELECT MAX(id) FROM utente));
SELECT setval('prodotto_id_seq', (SELECT MAX(id) FROM prodotto));
SELECT setval('commento_id_seq', (SELECT MAX(id) FROM commento));