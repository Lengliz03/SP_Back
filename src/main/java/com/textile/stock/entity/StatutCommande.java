package com.textile.stock.entity;

public enum StatutCommande {
    EN_ATTENTE, // Commande créée, en attente de réception
    RECUE, // Commande reçue, stock mis à jour
    ANNULEE // Commande annulée
}