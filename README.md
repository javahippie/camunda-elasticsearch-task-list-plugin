[![Build Status](https://travis-ci.org/javahippie/camunda-elasticsearch-task-list-plugin.svg?branch=master)](https://travis-ci.org/javahippie/camunda-elasticsearch-task-list-plugin)

# Camunda Elasticsearch Task List Process Engine Plugin
**Work in progress**

A Plugin for [Camunda BPM](http://docs.camunda.org) which automatically forwards all updates regarding user Tasks (create, assign, complete, delete) including their variables to an Elasticsearch instance. This enables users to full-text-search the existing tasks in a reasonable amount of time. 

## How to use it?
### For Tomcat
Copy the created fat-jar into the lib directory of the tomcat and add the following configuration to the plugins section of your bpm-platform.xml:

```
      <plugin>
        <class>de.javahippie.camunda.ElasticSearchTaskProcessEnginePlugin</class>
        <properties>
          <property name="clusterName">docker-cluster</property>
          <property name="port">9300</property>
          <property name="domainName">localhost</property>
        </properties>
      </plugin>
```
Configure the properties to match with your environment

## Environment Restrictions
Built and tested against Camunda BPM version 7.9.0.

## Known Limitations
Right now there is no error handling implemented, if your elasticsearch environment is not reachable anymore, your process engine won't be able to process any user tasks.

## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

<!-- HTML snippet for index page
  <tr>
    <td><img src="snippets/elasticsearch-task-plugin/src/main/resources/process.png" width="100"></td>
    <td><a href="snippets/elasticsearch-task-plugin">Camunda BPM Process Engine Plugin</a></td>
    <td>A Plugin for [Camunda BPM](http://docs.camunda.org).</td>
  </tr>
-->
<!-- Tweet
New @CamundaBPM example: Camunda BPM Process Engine Plugin - A Plugin for [Camunda BPM](http://docs.camunda.org). https://github.com/camunda/camunda-consulting/tree/master/snippets/elasticsearch-task-plugin
-->
