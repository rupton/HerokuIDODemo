# HerokuIDO Demo

This is a demo Heroku app for use with Salesforce.org Solution Engineering demos. This application is the second half of a two-part application demonstrating data archiving with Heroku, Salesforce, and Heroku Connect. This is a Java Jersey application defining an API for data archival. 
Data is passed from Salesforce to the Heroku application that copies and archives the data in a Heroku database. Heroku Connect External Objects in turn are used to sync archived data back to Salesforce for records that need to be unarchived.
This version currently only works on the Account object. Future versions will support multiple sObjects.

## Getting Started

Fork this repo and install it into you Heroku app or deploy using the Deploy to Heroku button. Once you have created your application, you will need to add PostgreSQL add-on to your Heorku app as well as Heroku Connect with External Objects
[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/rupton/HerokuIDODemo)

### Prerequisites

The Heroku application your deploy this to will need to have PostgreSQL and Heroku Connect installed in it to work properly. 

Check [here](https://devcenter.heroku.com/articles/getting-started-with-heroku-and-connect-without-local-dev#introduction) for details on working wit Heroku Connect

You can find more detail on installing Heroku Postgress [here](https://devcenter.heroku.com/categories/heroku-postgres)

### Installing

The simplest way to install the application is with the Deploy to Heroku Button


[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/rupton/HerokuIDODemo)

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc
