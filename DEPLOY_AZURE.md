# Provisionamento Azure SQL + Azure Container Apps — ConfereAí

> Pré-requisitos: [Azure CLI](https://learn.microsoft.com/cli/azure/install-azure-cli) instalado, `az login` feito e a imagem já publicada no Docker Hub pelo GitHub Actions.
> Se for o primeiro uso do Container Apps na assinatura: `az extension add --name containerapp --upgrade` e `az provider register --namespace Microsoft.App`.

## 1. Variáveis

### Bash

```bash
export RESOURCE_GROUP=rg-[rm]-confereai
export LOCATION=brazilsouth
export SQL_SERVER=[rm]-sql-confereai
export APP_CONTAINER=[rm]-confereai-api
export CONTAINERAPPS_ENVIRONMENT=[rm]-confereai-env

export DB_SERVER_URL=$SQL_SERVER.database.windows.net
export DB_SERVER_PORT=1433
export DB_SCHEMA=confereai
export DB_USER=sqladmin
export DB_PWD="<senha-forte>"

export SPRING_PROFILES_ACTIVE=prd
export DOCKER_IMAGE=docker.io/<usuario-dockerhub>/confereai-api:latest
```

### PowerShell

```powershell
$env:RESOURCE_GROUP="rg-[rm]-confereai"
$env:LOCATION="brazilsouth"
$env:SQL_SERVER="[rm]-sql-confereai"
$env:APP_CONTAINER="[rm]-confereai-api"
$env:CONTAINERAPPS_ENVIRONMENT="[rm]-confereai-env"

$env:DB_SERVER_URL="$env:SQL_SERVER.database.windows.net"
$env:DB_SERVER_PORT="1433"
$env:DB_SCHEMA="confereai"
$env:DB_USER="sqladmin"
$env:DB_PWD="<senha-forte>"

$env:SPRING_PROFILES_ACTIVE="prd"
$env:DOCKER_IMAGE="docker.io/<usuario-dockerhub>/confereai-api:latest"
```

> Nos comandos abaixo (Bash), no PowerShell troque `$VAR` por `$env:VAR` e `\` por `` ` ``.

## 2. Resource Group

```bash
az group create --name $RESOURCE_GROUP --location $LOCATION
```

## 3. SQL Server

```bash
az sql server create \
  --name $SQL_SERVER \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION \
  --admin-user $DB_USER \
  --admin-password "$DB_PWD"
```

## 4. SQL Database (camada gratuita)

```bash
az sql db create \
  --name $DB_SCHEMA \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER \
  --edition GeneralPurpose \
  --family Gen5 \
  --capacity 2 \
  --compute-model Serverless \
  --use-free-limit \
  --free-limit-exhaustion-behavior AutoPause
```

## 5. Firewall

Permitir serviços do Azure (o Container App) acessarem o banco:

```bash
az sql server firewall-rule create \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER \
  --name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0
```

Opcional — liberar o seu IP para consultar o banco do seu computador:

```bash
az sql server firewall-rule create \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER \
  --name MeuIP \
  --start-ip-address <seu-ip> \
  --end-ip-address <seu-ip>
```

## 6. Container Apps Environment

```bash
az containerapp env create \
  --name "$CONTAINERAPPS_ENVIRONMENT" \
  --resource-group "$RESOURCE_GROUP" \
  --location $LOCATION
```

## 7. Container App (senha guardada como secret)

```bash
az containerapp create \
  --name "$APP_CONTAINER" \
  --resource-group "$RESOURCE_GROUP" \
  --environment "$CONTAINERAPPS_ENVIRONMENT" \
  --image "$DOCKER_IMAGE" \
  --ingress external \
  --target-port 8080 \
  --min-replicas 1 \
  --max-replicas 1 \
  --cpu 0.5 --memory 1.0Gi \
  --secrets "db-pwd=$DB_PWD" \
  --env-vars \
    DB_SERVER_URL="$DB_SERVER_URL" \
    DB_SERVER_PORT="$DB_SERVER_PORT" \
    DB_SCHEMA="$DB_SCHEMA" \
    DB_USER="$DB_USER" \
    DB_PWD="secretref:db-pwd" \
    DB_ENCRYPT="true" \
    SPRING_PROFILES_ACTIVE="$SPRING_PROFILES_ACTIVE"
```

## 8. Status e logs

```bash
az containerapp show --name "$APP_CONTAINER" --resource-group "$RESOURCE_GROUP" \
  --query "properties.runningStatus" --output tsv

az containerapp logs show --name "$APP_CONTAINER" --resource-group "$RESOURCE_GROUP" \
  --type console --tail 100
```

## 9. Atualizar para uma nova imagem (redeploy manual)

```bash
az containerapp update \
  --name "$APP_CONTAINER" \
  --resource-group "$RESOURCE_GROUP" \
  --image "docker.io/<usuario-dockerhub>/confereai-api:<sha-ou-vN>"
```

## 10. Deploy automático pelo GitHub Actions

```bash
az ad sp create-for-rbac --name "gh-confereai" --role contributor \
  --scopes /subscriptions/<subscription-id>/resourceGroups/$RESOURCE_GROUP \
  --json-auth
```

No GitHub: secret `AZURE_CREDENTIALS` = JSON acima; variables `AZURE_RESOURCE_GROUP` e `AZURE_CONTAINER_APP`. O workflow `deploy.yml` passa a atualizar o app após cada CD.

## 11. Estrutura dos recursos

```text
Azure
└── Resource Group: rg-[rm]-confereai
    ├── SQL Server: [rm]-sql-confereai
    │   └── Database: confereai
    └── Container Apps Environment: [rm]-confereai-env
        └── Container App: [rm]-confereai-api
            └── Image: docker.io/<usuario>/confereai-api:latest
```

## 12. Fluxo

```text
App / Swagger ──HTTPS──> Azure Container App (confereai-api :8080)
                              │                    │
                              │ JDBC :1433         │ HTTPS
                              ▼                    ▼
                      Azure SQL Database      BrasilAPI (Receita / Banco Central)
                         confereai
```

## 14. Limpar tudo depois da avaliação

```bash
az group delete --name $RESOURCE_GROUP --yes --no-wait
```
