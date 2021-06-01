# Leave Application

This is a web based leave application developed by and for DeARX Services. This application forms part of the Java training series hosted by DeARX Cape Town and is being developed by the team for their 2021 practical project.

The application is was generated using [JHipster 6.10.5][] as a starting point, and developed further by custom modifications. The application is based on [Java 11](https://www.oracle.com/java/) for the back-end and [Angular](https://angular.io/) for the front-end. It uses [Maven](https://maven.apache.org/) for dependency management in Java and [NPM](https://www.npmjs.com/) for the same in the front-end.

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

### NodeJS:

We use [Node.js][] to run a development web server and build the project. Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

We use npm scripts and [Angular CLI][] with [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```
./mvnw
npm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### Java

The back-end application uses the Java 11 Java Development Kit, version 11 LTS or later. Ensure that your JAVA_HOME environent variable is set.

### JHipster

Install JHipster using NPM;

```
npm install -g generator-jhipster
```

### Environment variables

Remember to setup your JAVA_HOME and MVN_HOME environment variables and include those (with the correct reference to the /bin directory) to your path. Ensure that any other Java install directories are removed from the path.

### Optional installations

#### Yeoman

If you want to use a module or a blueprint (for instance from the JHipster Marketplace), install Yeoman:

```
npm install -g yo
```

### Testing your installation

Test your installation with the following commands;

```shell
$ java -version
$ mvn -v
$ git --version
```

### Configure git

Setup your git installation by supplying your name, email address and, a requirement for Windows installations, the line feed / carriage return setting;

```shell
$ git config --global user.name "John Doe"
$ git config --global user.email johndoe@example.com
$ git config --global core.autocrlf true
```

NOTE: Windows users are required to set the autocrlf property!!

Run the following command to confirm these settings have been applied;

```shell
$ git config --list
```

### Integrated development environment

We recommend one of the following IDE's for development;

- Spring STS
- Eclipse Neon or later

## Version control

The project uses Git as version control mechanism, together with GitFlow as a release methodology (see later in this document). As a result the main branches are;

- master - Stable, production ready code. The master branch will also have the release tags for the production releases.
- development - Stable development code. This usually represents the latest code.
- feature/\* - In-development, feature branches, representing new features not yet integrated.
- release/\* - Temporary branches for code being prepared for release
- hotfix/\* - Temporary branches for hotfixes in master. Branched off master
- support/\* - Support branches for code in production. Branched off master
- support/x/\* - Temporary branches for hotfixes in support branches. Branced off x.

### Checking the code out for the first time

Use the link provided at the top of the repository page as the URL to clone the project from.

```shell
$ git clone https://github.com/christhonie/leave-application.git
```

Note that both SSH or HTTPS can be used. The former is preferred, which elimintate the need to use a password (if the key used is not protected by a password). The setup of SSH is ourside the scope of this documents.

### Protected branches

Users could be limited in pushing or merging protected branches. This is dependent on the configuration of the Git repository. In the case of protected branches the developer should create feature, release or hotfix branches, which will be considered for inclusion into the project by a project leader using a merge request. See the procedure to be followed below.

### References

To become familiar with Git, GitFlow and how to migrate from SVN see these guides;

Git Tutorials: https://www.atlassian.com/git/tutorials
What is Git?: https://www.atlassian.com/git/tutorials/what-is-git
Git Sheet Sheet: https://www.atlassian.com/git/tutorials/atlassian-git-cheatsheet
Migrating from SVN to Git: https://git.or.cz/course/svn.html

## Your first build

The first build is best performed from the command line. Use the following command to get started;

```shell
mvn clean install
```

## Build instructions

Always use the clean goal when building the project for deployment.

Run the install goal from the root directory;

```shell
mvn clean install
```

This will ensure that all the dependencies are compiled and installed on your local M2 Maven repository.

Applications should be run from your local PC to ensure that the code actually run. This process will also execute any included unit and integration tests.

```shell
mvn spring-boot:run
```

## Deployment to the development servers

We use Cargo to deploy to the local DEV Tomcat instances. From the Core directory run;

```shell
mvn -Pwar clean install cargo:redeploy
```

## Release process

This project makes use of GitFlow for release management.

```shell
mvn gitflow:release-start
```

Apply any additional version changes, prepare DB migration scripts, generate documentation.

```shell
mvn gitflow:release-finish
```

## Development

### Configuration files

The system primarily uses Spring Configuration to manage the system configuration variables.

#### Classpath application.properties

This is the top level configuration file, found in the class path and also at /src/main/java/resources, includes all global default values at design time.

This file should NEVER include environment specific values, such as URLs. It is better to leave such values unassigned, so that they can be specified in the subsequent files. It is better for the application to fail at start-up when mandatory, environment specific values are missing, rather that setting default ones.

#### Classpath application-server.properties

This is the second level configuration file, found in the class path and also at /src/main/java/resources, is used by the servlet (server) version of the application. It should include configuration values specific to a Tomcat deployment.

This file should also NEVER include environment specific values (see above).

#### Classpath application-development.properties

This is the second level configuration file used for development builds, using the embedded Tomcat server. Use this file for any configuration files specific to your development machine or when debugging on it. This file is excluded from Git and therefore contains your personal settings.

#### Classpath application-development.properties.sample

This is the template file for application-development.properties. During the build, if the application-development.properties file is missing, the sample file is used as an initial version.

#### External application-server.properties

Versions of the application deployed to Tomcat servers can be configured to use an external configuration file. This is desirable in production and QA environments, where it is not practical to recompile the application to make configuration changes.

This file is intended to include environment specific values, which make it easy for an IT person to make changes if and when required. Typically the server should be restarted for the changes to take effect.

#### logback-spring.xml

This is the logback (logging) configuration file. It defines the default logging level of the various loggers and defines the appenders to use (file and logging server).

All file based appenders which logs continiously MUST implement a rolling file appender or similar. This will ensure that old log files are first compressed for storage and eventually purged. The retention period of log files should typically not be longer than one month. When using a logging server it should be set much shorter, like 3-7 days.

Projects could be configured to log to LogStash, a centralised logging server. The LogStash appender is responsible for shipping log files to this server in an asynchronous manner.

### PWA Support

This application ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

The service worker initialization code is disabled by default. To enable it, uncomment the following code in `src/main/webapp/app/app.module.ts`:

```typescript
ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
```

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
npm install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
npm install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Edit [src/main/webapp/app/app.module.ts](src/main/webapp/app/app.module.ts) file:

```
import 'leaflet/dist/leaflet.js';
```

Edit [src/main/webapp/content/scss/vendor.scss](src/main/webapp/content/scss/vendor.scss) file:

```
@import '~leaflet/dist/leaflet.css';
```

Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
```

## Building for production

### Packaging as jar

To build the final jar and optimize the Leave Application application for production, run:

```
./mvnw -Pprod clean verify
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```
java -jar target/*.jar
```

Then navigate to [http://localhost:8088](http://localhost:8088) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Client tests

Unit tests are run by [Jest][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

```
npm test
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mysql database in a docker container, run:

```
docker-compose -f src/main/docker/mysql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/mysql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./mvnw -Pprod verify jib:dockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 7.0.1 archive]: https://www.jhipster.tech/documentation-archive/v7.0.1
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v7.0.1/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v7.0.1/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v7.0.1/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v7.0.1/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v7.0.1/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v7.0.1/setting-up-ci/
[node.js]: https://nodejs.org/
[webpack]: https://webpack.github.io/
[angular cli]: https://cli.angular.io/
[browsersync]: https://www.browsersync.io/
[jest]: https://facebook.github.io/jest/
[jasmine]: https://jasmine.github.io/2.0/introduction.html
[protractor]: https://angular.github.io/protractor/
[leaflet]: https://leafletjs.com/
[definitelytyped]: https://definitelytyped.org/
