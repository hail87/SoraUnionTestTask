
delete from lwa_sandbox.accountAddress
where addressID = ( SELECT addressID FROM lwa_sandbox.address where firstName = 'onhUmoeWQg1ptjEMR3Q3Aw==' and lastName = 'BzweC+nKQsZVFZ/w7McSLQ==');

delete from lwa_sandbox.address
where firstName = 'onhUmoeWQg1ptjEMR3Q3Aw==' and lastName = 'BzweC+nKQsZVFZ/w7McSLQ==';

--delete from lwa_sandbox.accountAddress
--where addressID = ( SELECT addressID FROM lwa_sandbox.address where firstName = 'Erika' and lastName = 'Spoon');
--
--delete from lwa_sandbox.address
--where firstName = 'Erika' and lastName = 'Spoon';