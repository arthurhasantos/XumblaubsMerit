@echo off
echo ========================================
echo    PARANDO PROCESSOS NA PORTA 8080
echo ========================================
echo.

echo Verificando processos na porta 8080...
netstat -ano | findstr :8080

echo.
echo Parando todos os processos na porta 8080...

for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    echo.
    echo Encontrado processo com PID: %%a
    echo Tentando parar o processo...
    taskkill /PID %%a /F
    if %errorlevel% equ 0 (
        echo ✓ Processo %%a parado com sucesso!
    ) else (
        echo ✗ Falha ao parar processo %%a (pode já estar parado ou sem permissão)
    )
)

echo.
echo ========================================
echo    VERIFICACAO FINAL
echo ========================================
echo Verificando se a porta 8080 está livre...
netstat -ano | findstr :8080

if %errorlevel% equ 1 (
    echo ✓ PORTA 8080 ESTA LIVRE! Pode rodar o backend agora.
) else (
    echo ✗ Ainda há processos na porta 8080.
)

echo.
echo ========================================
echo    COMANDOS PARA RODAR O BACKEND:
echo ========================================
echo 1. mvn spring-boot:run
echo 2. Ou use: mvn clean spring-boot:run
echo ========================================
pause
