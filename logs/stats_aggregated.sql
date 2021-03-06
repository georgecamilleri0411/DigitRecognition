WITH
	tf AS (SELECT TestID, AVG(CAST((CAST(SuccessfulClassifications AS DECIMAL (10, 6)) * 100 / CAST(NumberOfTuples AS DECIMAL (10, 6))) AS DECIMAL (10, 6))) AS 'Success' FROM tb_ds_stats INNER JOIN tb_ds_datasets ON tb_ds_datasets.pk = fk_TestDataset GROUP BY TestID)
	
SELECT
	s.TestID
	, a.AlgorithmName
	, s.kValue
	, CASE 
		WHEN MAX(ds2.Description) LIKE 'Default%' THEN 'Default'
		WHEN MAX(ds2.Description) = 'Shuffled dataset 2' THEN 'Shuffled datasets 1/2'
		WHEN MAX(ds2.Description) = 'Shuffled dataset 4' THEN 'Shuffled datasets 3/4'
	END AS 'Dataset used'
	, SUM(ds2.NumberOfTuples) AS 'Total Number of Tests'
	, tf.Success AS 'Two-Fold Success %age'
FROM
	dbo.tb_ds_stats s
		INNER JOIN tb_algorithms a ON a.pk = s.fk_algorithm
		INNER JOIN tb_ds_datasets ds1 ON ds1.pk = s.fk_TrainingDataset
		INNER JOIN tb_ds_datasets ds2 ON ds2.pk = s.fk_TestDataset
		INNER JOIN tf ON tf.TestID = s.TestID
GROUP BY 
	s.TestID
	, a.AlgorithmName
	, s.kValue
	, tf.Success
ORDER BY
	tf.Success DESC
	, TestID