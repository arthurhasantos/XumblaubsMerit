@echo off
echo ========================================
echo    TESTE SIMPLES DE LOGIN
echo ========================================
echo.

echo Testando login com curl...
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"admin@admin.com\",\"senha\":\"admin123\"}" ^
  -v

echo.
echo ========================================
echo    VERIFIQUE OS LOGS DO BACKEND
echo ========================================
echo Procure por estas mensagens nos logs:
echo - "Tentativa de login para: admin@admin.com"
echo - "Usuário encontrado: admin@admin.com - Tipo: ADMIN"
echo - "Senha válida: true"
echo - "Gerando token para: admin@admin.com - Tipo: ADMIN"
echo - "Token gerado com sucesso!"
echo - "Login realizado com sucesso!"
echo ========================================
pause
