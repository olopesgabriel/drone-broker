. ./config.sh

scp -i ${key} ../target/AppBroker-1.0.0-jar-with-dependencies.jar ${host}:~/app-broker.jar
scp -i ${key} ./start.sh ${host}:~/
ssh -i ${key} ${host} sudo fuser -k 1234/tcp

ssh -i ${key} ${host} chmod +x start.sh
ssh -i ${key} ${host} ./start.sh &
