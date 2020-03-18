# cmdb-server

The Service reads a given CMDB Repo path via a HTTP Request, handles Authentication and delivers the requested properties as JSON. It also integrates the features for CI, which are shown in the [Gradle-Jib Repository](https://github.com/gluehbirnenkopf/gradle-jib)

## Get API Spec, to be done by HATEOAS in future
```bash
http://localhost:8080/api/
```

## Request a File from CMDB
```bash
http://localhost:8080/api/getProperties/{gitCmdbBranch}/{filePathInGitRepo}"
# Path in GitHub:
https://github.ibmgcloud.net/blw-msa/CMDB/blob/master/cloud/jira.properties
# API Request:
http://localhost:8080/api/getProperties/master/cloud/jira.properties
```

## ToDo:
* Package names to be changed
* Unit Tests
* Helm Chart for Deployment
* Json and Yaml Support, not only Propertie Files
* make it really RESTful
* exploration of the CMDB Repo via api
* Single Property retrieval
* Write Properties

## Run Locally:
```bash
./gradlew bootrun
```
