#FROM tomcat:latest
#EXPOSE 8081
#ARG WAR_FILE=target/TrackLoader-1.0-SNAPSHOT.war
#COPY ${WAR_FILE} /usr/local/tomcat/webapps/Root.war

FROM tomcat:latest

# Remove the default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your WAR file into the container at the specified Tomcat webapps directory
COPY ./target/TrackLoader-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose the port your Tomcat will run on
EXPOSE 8081

# Start Tomcat
CMD ["catalina.sh", "run"]