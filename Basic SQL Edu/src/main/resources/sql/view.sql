
SELECT DISTINCT
    aa.th_code, aa.de_code, aa.ver_data, bb.ver_data, cc.ver_data, dd.ver_data, ee.ver_data, ff.ver_data
FROM
    (SELECT de_code, th_code, ver_data FROM quiz_detail
    WHERE ver_name = 'animal_size') aa
    ,(SELECT de_code, th_code, ver_data FROM quiz_detail
    WHERE ver_name = 'species') bb
    ,(SELECT de_code, th_code, ver_data FROM quiz_detail
    WHERE ver_name = 'legs') cc
    ,(SELECT de_code, th_code, ver_data FROM quiz_detail
    WHERE ver_name = 'color') dd
    ,(SELECT de_code, th_code, ver_data FROM quiz_detail
    WHERE ver_name = 'habitat') ee
    ,(SELECT de_code, th_code, ver_data FROM quiz_detail
    WHERE ver_name = 'feed') ff
WHERE
    aa.th_code = bb.th_code
    AND
    bb.th_code = cc.th_code
    AND
    cc.th_code = dd.th_code
    AND
    dd.th_code = ee.th_code
    AND
    ee.th_code = ff.th_code
ORDER BY TH_CODE;

select
    gp.gp_code
    ,gp.gp_name
    ,th.th_code
    ,th.th_name
    ,de.de_code
    ,de.ver_name
    ,de.ver_data
from
    quiz_group gp,quiz_theme th, quiz_detail de
where
    gp.gp_code = 1
and
    gp.gp_code = th.gp_code
and
    th.th_code = de.th_code;

--  종 | 사이즈 |색깔 | 다리 수 | 서식지 | 먹는것
-- 사자 , s      r    4      land   meat
-- 사자    b      r    3       land   meat 
-- 호랑이  b     b     4      sky     meat
