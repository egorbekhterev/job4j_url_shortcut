# Создание Secret
kubectl apply -f postgresdb-secret.yml
sleep 3
kubectl get secrets
sleep 3

# Создание ConfigMap
kubectl apply -f postgresdb-configmap.yml
sleep 3
kubectl get configmaps
sleep 3

# Создание развертывания postgresdb-deployment
kubectl apply -f postgresdb-deployment.yml
sleep 5
kubectl logs -l app=postgresdb
sleep 5
kubectl describe pod

# Создание развертывания spring-deployment
kubectl apply -f spring-deployment.yml
sleep 5
kubectl logs -l app=spring-boot
sleep 5
kubectl describe pod