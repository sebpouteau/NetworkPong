#!/usr/bin/scapy

# créer un nouveau paquet IP
paquet_IP = IP()
# regardons les valeurs par défaut
paquet_IP.show()
# adresse source
paquet_IP.src = "10.0.0.1"
# adresse destination:
paquet_IP.dst = "10.0.0.2"
# voyons le résultat
paquet_IP.show()

# créer le datagramme UDP
datagramme_UDP = UDP()
datagramme_UDP.show()
# port source arbitraire
datagramme_UDP.sport = 12345
# port destination daytime
datagramme_UDP.dport = 13
datagramme_UDP.show()
# pas de données

# encapsuler le datagramme UDP dans le paquet IP (et pas l'inverse !)
# le '/' ici n'est donc pas une division, il a été redéfini
paquet = paquet_IP/datagramme_UDP
# observer le champ proto mis à jour automatiquement
paquet.show()
# on envoie (les checksums sont calculés automatiquement) et on récupère la réponse
reponse = sr1(paquet)

# regardons la réponse
reponse.show()

# extraire le datagramme UDP
reponse_UDP = reponse.payload
# voyons la taille
reponse_UDP.len
# extraire le contenu du datagramme UDP
payload = reponse_UDP.payload
# extraire les données
date = payload.load
# et regarder
date
