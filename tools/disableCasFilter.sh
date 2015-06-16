echo "Disabling CAS Authentication Filter."

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

webXml="${DIR}/../src/main/webapp/WEB-INF/web.xml"

sed -i 's/<!-- CAS FILTER START -->/<!-- CAS FILTER START/' ${webXml}
sed -i 's/<!-- CAS FILTER END -->/CAS FILTER END -->/' ${webXml}
