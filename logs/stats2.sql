WITH
	tf AS (SELECT TestID, AVG(CAST((CAST(SuccessfulClassifications AS DECIMAL (10, 6)) * 100 / CAST(NumberOfTuples AS DECIMAL (10, 6))) AS DECIMAL (10, 6))) AS 'Success' FROM tb_ds_stats INNER JOIN tb_ds_datasets ON tb_ds_datasets.pk = fk_TestDataset GROUP BY TestID)
	
SELECT
	s.pk AS 'TestID'
	, s.TestID AS 'Two-Fold Test ID'
	, a.AlgorithmName
	, s.kValue
	, ds1.Description AS 'Training Dataset'
	, ds2.Description AS 'Test Dataset'
	, CAST((CAST(s.SuccessfulClassifications AS DECIMAL (10, 6)) * 100 / CAST(ds2.NumberOfTuples AS DECIMAL (10, 6))) AS DECIMAL (10, 6)) AS 'Success %age'
	, ds2.NumberOfTuples AS 'Number of Tests'
	, s.TwoFoldVersion
	, tf.Success AS 'Two-Fold Success %age'
FROM
	dbo.tb_ds_stats s
		INNER JOIN tb_algorithms a ON a.pk = s.fk_algorithm
		INNER JOIN tb_ds_datasets ds1 ON ds1.pk = s.fk_TrainingDataset
		INNER JOIN tb_ds_datasets ds2 ON ds2.pk = s.fk_TestDataset
		INNER JOIN tf ON tf.TestID = s.TestID