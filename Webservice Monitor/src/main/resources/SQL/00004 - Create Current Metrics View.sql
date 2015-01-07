CREATE VIEW `service_monitor`.`webservice_current_metrics` AS
    SELECT 
        `wm`.`name` AS `name`,
        `wm`.`status` AS `status`,
        `wm`.`response_time` AS `response_time`,
        `wm`.`date` AS `date`
    FROM
        `webservice_metrics` `wm`
    WHERE
        (`wm`.`date` = (SELECT 
                MAX(`webservice_metrics`.`date`)
            FROM
                `webservice_metrics`
            WHERE
                (`webservice_metrics`.`name` = `wm`.`name`)))