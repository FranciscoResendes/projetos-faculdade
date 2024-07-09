--SIBD 2022/23
-- Grupo 7
--Diogo Freaza       56969  TP12
--Guilherme Dias     57163  TP12
--Francisco Resendes 57162  TP13

-----------------------------------------------------------------------------------------------
--                                     Query 1
-----------------------------------------------------------------------------------------------
SELECT C.nif, C.nome, (TO_CHAR(F.data, 'YYYY') - C.nascimento) AS idade, P.ean13, P.nome AS nome_do_produto, F.numero, F.data 
  FROM cliente C, produto P, fatura F, linhafatura L 
 WHERE (P.ean13 = L.produto)
   AND (F.numero = L.fatura)
   AND (C.nif = F.cliente)
   AND (C.nome LIKE '%Dias%')
   AND (C.genero = 'F')
   AND (TO_CHAR(F.data, 'YYYY') = '2021')
   ORDER BY C.nascimento DESC, C.nome ASC, F.data DESC, P.nome DESC;

-----------------------------------------------------------------------------------------------
--                                     Query 2
-----------------------------------------------------------------------------------------------

   SELECT C.nif, C.nome
   FROM cliente C, produto P, fatura F, linhafatura L
  WHERE (P.ean13 = L.produto)
    AND (F.numero = L.fatura)
    AND (C.nif = F.cliente)
    AND (C.genero = 'M')
    AND ('Beleza' <> P.categoria)
    AND (c.nif NOT IN (SELECT C1.nif  --Verifica que o cliente selecionado nunca comprou produtos de beleza
                      FROM cliente C1, produto P1, fatura F1, linhafatura L1
                        WHERE (P1.ean13 = L1.produto)
                        AND (F1.numero = L1.fatura)
                        AND (C1.nif = F1.cliente)
                        AND ('Beleza' = P1.categoria)))
    INTERSECT -- Interseta os clientes que não compraram produtos de beleza com clientes que compraram roupa em menos de 3 ocasiões
  SELECT C.nif, C.nome
    FROM cliente C, fatura F
    WHERE (F.cliente = C.nif)
    AND (TO_CHAR(F.data, 'YYYY') = '2021')
    AND (F.numero IN (SELECT F1.numero   -- Verifica se a fatura é uma fatura que tem roupa 
                        FROM cliente C1, produto P1, fatura F1, linhafatura L1
                        WHERE (P1.ean13 = L1.produto)
                        AND (F1.numero = L1.fatura)
                        AND (C1.nif = F1.cliente)
                        AND (P1.categoria = 'Roupa')))
    GROUP BY (C.nif, C.nome)
    HAVING COUNT(F.cliente) BETWEEN 0 AND 2
    UNION -- une com os clientes que não tem faturas
    SELECT C.nif AS nif, C.nome AS nome
    FROM cliente C
    LEFT JOIN fatura F ON C.nif = F.cliente
    WHERE (f.numero IS NULL)
    AND (C.genero = 'M')
    ORDER BY nome ASC, nif DESC;




-----------------------------------------------------------------------------------------------
--                                     Query 3
-----------------------------------------------------------------------------------------------

SELECT P.ean13, P.preco, P.nome, P.categoria,  P.stock
  FROM produto P 
  WHERE (P.preco < (SELECT AVG(P2.preco)                             -- Condição para verificar que só aparecem os produtos com preço abaixo da média
                      FROM produto P2))
INTERSECT
SELECT P.ean13 AS ean13, P.preco AS preco, P.nome AS nome, P.categoria AS categoria, P.stock AS stock
  FROM cliente C, produto P, fatura F, linhafatura L
WHERE (P.ean13 = L.produto)
  AND (F.numero = L.fatura)
  AND (C.nif = F.cliente)
  AND (P.categoria = 'Comida')
  AND (C.localidade = 'Porto')
  AND (TO_CHAR(F.data, 'HH24') BETWEEN 8 AND 12)
GROUP BY (P.ean13, P.nome, P.categoria, P.preco, P.stock)
HAVING COUNT( DISTINCT C.nif) IN (SELECT COUNT (C.nif)               -- Verifica se o numero de vezes que um produto foi comprado é igual ao numero de pessoas de clientes do porto
                                    FROM cliente C                   -- Se 1 cliente do porto comprou 1 produto 2x só conta uma delas por causa do COUNT (DISTINCT)
                                    WHERE (C.localidade = 'Porto'))
ORDER BY preco DESC, ean13 ASC;

-----------------------------------------------------------------------------------------------
--                                     Query 4
-----------------------------------------------------------------------------------------------

    SELECT  C.nif AS nif, C.nome AS nome, C.genero AS genero, TO_CHAR(F.data, 'YYYY') AS ano, SUM (P.preco * L.unidades) AS total
    FROM cliente C, produto P, fatura F, linhafatura L 
    WHERE (P.ean13 = L.produto)
      AND (F.numero = L.fatura)
      AND (C.nif = F.cliente)
      GROUP BY (C.nif, C.nome, C.genero, TO_CHAR(F.data, 'YYYY'))
      HAVING  SUM (P.preco * L.unidades) IN (SELECT  MAX(S.total)      --O HAVING vai ver se o total gasto pelo cliente foi o máximo gasto em algum ano (género M)
                                                FROM (SELECT  C1.nif AS nif, C1.nome AS nome, TO_CHAR(F1.data, 'YYYY') AS ano, SUM (P1.preco * L1.unidades) total
                                                      FROM cliente C1, produto P1, fatura F1, linhafatura L1 
                                                      WHERE (P1.ean13 = L1.produto)
                                                        AND (F1.numero = L1.fatura)
                                                        AND (C1.nif = F1.cliente)
                                                        AND (C1.genero = 'M')
                                                        GROUP BY (C1.nif, C1.nome, TO_CHAR(F1.data, 'YYYY'))
                                                        ORDER BY ano ASC) S
                                            GROUP BY (S.ano))
   UNION
   SELECT  C.nif AS nif, C.nome AS nome, C.genero AS genero, TO_CHAR(F.data, 'YYYY') AS ano, SUM (P.preco * L.unidades) AS total
     FROM cliente C, produto P, fatura F, linhafatura L 
    WHERE (P.ean13 = L.produto)
      AND (F.numero = L.fatura)
      AND (C.nif = F.cliente)
      GROUP BY (C.nif, C.nome, C.genero, TO_CHAR(F.data, 'YYYY'))
      HAVING  SUM (P.preco * L.unidades) IN (SELECT  MAX(S.total)   --O HAVING vai ver se o total gasto pelo cliente foi o máximo gasto em algum ano (género F)
                                                FROM (SELECT  C1.nif AS nif, C1.nome AS nome, TO_CHAR(F1.data, 'YYYY') AS ano, SUM (P1.preco * L1.unidades) total
                                                      FROM cliente C1, produto P1, fatura F1, linhafatura L1 
                                                      WHERE (P1.ean13 = L1.produto)
                                                        AND (F1.numero = L1.fatura)
                                                        AND (C1.nif = F1.cliente)
                                                        AND (C1.genero = 'F')
                                                        GROUP BY (C1.nif, C1.nome, TO_CHAR(F1.data, 'YYYY'))
                                                        ORDER BY ano ASC) S
                                            GROUP BY (S.ano))
      ORDER BY ano DESC, genero ASC;
