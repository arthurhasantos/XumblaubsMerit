@echo off
echo ========================================
echo    TESTE DE AUTENTICACAO - Sistema de Merito
echo ========================================
echo.

echo 1. Testando Health Check (publico)...
curl -X GET http://localhost:8080/api/health
echo.
echo.

echo 2. Tentando acessar CRUD sem autenticacao (deve falhar)...
curl -X GET http://localhost:8080/api/alunos
echo.
echo.

echo 3. Fazendo login como ADMIN...
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"admin@admin.com\",\"senha\":\"admin123\"}"
echo.
echo.

echo 4. Validando token (substitua TOKEN_AQUI pelo token retornado acima)...
echo curl -X POST http://localhost:8080/api/auth/validate ^
echo   -H "Authorization: Bearer TOKEN_AQUI"
echo.
echo.

echo 5. Testando acesso aos CRUDs com token (substitua TOKEN_AQUI)...
echo curl -X GET http://localhost:8080/api/alunos ^
echo   -H "Authorization: Bearer TOKEN_AQUI"
echo.
echo.

echo ========================================
echo    INSTRUCOES:
echo ========================================
echo 1. Execute o passo 3 para obter o token
echo 2. Copie o token da resposta
echo 3. Substitua TOKEN_AQUI pelo token nos comandos 4 e 5
echo 4. Execute os comandos 4 e 5 manualmente
echo ========================================
pause
