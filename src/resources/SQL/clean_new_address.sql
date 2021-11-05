delete from lwa_sandbox.accountAddress
where addressID = (SELECT addressID FROM lwa_sandbox.address where lastName = 'FBpmxSt++r8j0+MZOjk+8w==');
--order by accountAddressID desc limit 1;

delete from lwa_sandbox.address
where lastName = 'FBpmxSt++r8j0+MZOjk+8w==';
--order by addressID desc limit 1;
