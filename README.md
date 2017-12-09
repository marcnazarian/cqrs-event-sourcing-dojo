# Coding dojo organized by Software Craftsmanship Grenoble ([@SoftCraftGre](https://twitter.com/SoftCraftGre)) about CQRS and Event Sourcing

Workshop presented by [Clément](https://twitter.com/clem_bouiller), [Emilien](https://twitter.com/ouarzy), [Florent](https://twitter.com/florentpellet).

Full description available: http://devlyon.fr/mixter



## Goal 

Implement a quacker (tweeter-like)

## Rules (introduced one by one)

* When a user quack Then it should raise a MessageQuacked event
* When a user delete a message Then it should raise a MessageDeleted event
* When a user delete a deleted message, Then it should *not* raise anything
* When a user quack an empty message (empty content) Then it should *not* raise anything
* When a user quack a too long message (42 chars max) Then it should *not* raise anything
* When a user quack a message with some inappropriate content (let's say the 'F' word) Then it should *not* raise anything
* Only message's authors can delete their messages
* When a user re-quack a message, Then it should raise a MessageReQuacked event
* When a user re-quack a message, Then it should increment the number of re-quacks

## Solution proposée par Florent et Emilien (en français)

Vidéo [CQRS & EventSourcing from scratch (Emilien Pecoul - Florent Pellet)](https://www.youtube.com/watch?v=S1V4t7SXXCU) donnée à Devoxx en Avril 2017

* 0.00.00 => intro
* 0.02.45 => jeu de role (architecte qui veut vendre du CRUD tout prêt à l'emploi au client)
* 0.13.40 => présentation Event Sourcing
* 0.19.53 =>  |_ questions/réponses
* 0.28.54 => présentation CQRS
* 0.37.25 =>  |_ questions/réponses
* 0.53.10 => présentation (brève) de Event Storming, DDD appliqués à l'application "mixter" qu'ils vont développer après
* 1.03.42 => les limites de CQRS et EventStorming
* 1.06.56 =>   sessions questions/réponses
* 1.26.25 => live coding (from scratch): Event Sourcing et C de CQRS sur application Mixter (twitter-like)
* 1.53.15 =>  |_ questions/réponses
* 1.59.34 => live coding: suite de Mixter avec le Q de CQRS (affichage de la timeline)
* 2.08.15 => live coding: synchro entre commande et projection
* 2.30.00 => conclusion
