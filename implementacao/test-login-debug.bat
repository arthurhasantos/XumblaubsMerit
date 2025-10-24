@echo off
echo ========================================
echo    TESTE DE LOGIN - DEBUG
echo ========================================
echo.

echo 1. Testando Health Check...
curl -X GET http://localhost:8080/api/health
echo.
echo.

echo 2. Testando login com credenciais corretas...
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"admin@admin.com\",\"senha\":\"admin123\"}"
echo.
echo.

echo 3. Testando login com email vazio...
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"\",\"senha\":\"admin123\"}"
echo.
echo.

echo 4. Testando login com senha vazia...
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"admin@admin.com\",\"senha\":\"\"}"
echo.
echo.

echo 5. Testando login com credenciais incorretas...
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"admin@admin.com\",\"senha\":\"senhaerrada\"}"
echo.
echo.

echo ========================================
echo    VERIFIQUE OS LOGS DO BACKEND
echo ========================================
echo Os logs devem aparecer no terminal onde o backend está rodando.
echo Procure por mensagens como:
echo - "Tentativa de login para: admin@admin.com"
echo - "Usuário encontrado: admin@admin.com - Tipo: ADMIN"
echo - "Senha válida: true/false"
echo ========================================
pause
