echo "Disabling CAS Authentication Filter."

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
bin=${DIR}/../bin
lib=${DIR}/../lib

webXml=${DIR}/../src/main/webapp/WEB-INF/web.xml

sed 's/<!-- CAS FILTER START -->/<!-- CAS FILTER START/' ${webXml}
sed 's/<!-- CAS FILTER END -->/CAS FILTER END -->/' ${webXml}
