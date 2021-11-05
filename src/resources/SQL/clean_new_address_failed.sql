
delete from lwa_sandbox.accountAddress
where addressID = ( SELECT addressID FROM lwa_sandbox.address where lastName = 'IECCrPzcm85scw6YQCrSzw==' );

delete from lwa_sandbox.address where lastName = 'IECCrPzcm85scw6YQCrSzw==';