echo "==========================="
echo "KILLING PREVIOUS BOT BUILDS"
echo "==========================="
kill `jps -l | grep "club-1.0-jar-with-dependencies.jar" | cut -d " " -f 1`

echo "=========================="
echo "PULLING LATEST GIT VERSION"
echo "=========================="
git pull
echo "-- Finished git pull --"

echo "============================"
echo "BUILDING LASTEST MAVEN BUILD"
echo "============================"
mvn package -f ./civassist/pom.xml
echo "-- Finished maven build --"

echo "============"
echo "STARTING BOT"
echo "============"
sh ./start.sh