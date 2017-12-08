# Coding dojo organized by Software Craftsmanship Grenoble ([@SoftCraftGre](https://twitter.com/SoftCraftGre)) about CQRS and Event Sourcing

Workshop presented by [Clément](https://twitter.com/clem_bouiller), [Emilien](https://twitter.com/ouarzy), [Florent](https://twitter.com/florentpellet).

Full description available: http://devlyon.fr/mixter



## Goal 

Implement a quacker (tweeter-like)

## Rules (introduced one by one)

* When a user quack Then it should raise a MessageQuacked event
* When a user delete a quack Then it should raise a MessageDeleted event
* When a user delete a deleted quack, Then it should *not* raise anything
* Only the quack's author should be able to delete its own quack
* When a user re-quack a quack, Then it should raise a MessageReQuacked event
* When a user quack an empty quack (empty content) Then it should *not* raise anything
* When a user quack a too long quack (42 chars max) Then it should *not* raise anything
* When a user quack a message with some inappropriate content (let's say the F word) Then it should *not* raise anything
* The author of a quack *cannot* re-quack its own quack
