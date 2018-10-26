## Jackhammer: 
One Security vulnerability assessment/management tool to solve all the security team problems.

## What is Jackhammer?

Jackhammer is a collaboration tool built with an aim of bridging the gap between Security team vs dev team, QA team and being a facilitator for TPM to understand and track the quality of the code going into production. It could do static code analysis and dynamic analysis with inbuilt vulnerability management capability. It finds security vulnerabilities in the target applications and it helps security teams to manage the chaos in this new age of continuous integration and continuous/multiple deployments.

It completely works on RBAC (Role Based Access Control). There are cool dashboards for individual scans and team scans giving ample flexibility to collaborate with different teams. It is totally built on pluggable architecture which can be integrated with any open source/commercial tool.


Jackhammer uses the OWASP pipeline project to run multiple open source and commercial tools against your code,web app, mobile app, cms (wordpress), network.


## Key Features:

* Provides unified interface to collaborate on findings
* Scanning (code) can be done for all code management repositories
* Scheduling of scans based on intervals # daily, weekly, monthly
* Advanced false positive filtering
* Publish vulnerabilities to bug tracking systems
* Keep a tab on statistics and vulnerability trends in your applications
* Integrates with majority of open source and commercial scanning tools
* Users and Roles management giving greater control
* Configurable severity levels on list of findings across the applications
* Built-in vulnerability status progression
* Easy to use filters to review targeted sets from tons of vulnerabilities
* Asynchronous scanning (via sidekiq) that scale
* Seamless Vulnerability Management
* Track statistics and graph security trends in your applications
* Easily integrates with a variety of open source, commercial and custom scanning tools



## Supported Vulnerability Scanners:

### Static Analysis:

 * [Brakeman][]
 * [Bundler-Audit][] 
 * [Dawnscanner][]
 * [FindSecurityBugs][]
 * [Xanitizer*][]
 * [NodeSecurityProject][]
 * [Retire.js][]


 &nbsp;&nbsp; * license required
 &nbsp;&nbsp;&nbsp;&nbsp; ** commercial license required


## Finding hard coded secrets/tokens/creds:

  * [Trufflehog][] (Slightly modified/extended for better result and integration as of May 2017)

## Webapp:

  * [Arachni][] 

## Mobile App:

  * [Androbugs][] (Slightly modified/extended for better result and integration as of May 2017)
  * [Androguard][] (Slightly modified/extended for better result and integration as of May 2017)

## Wordpress:

   * [WPScan][] (Slightly modified/extended for better result and integration as of May 2017)

## Network:

  * [Nmap][] 

## Adding Custom (other open source/commercial /personal) Scanners:

   You can add any scanner to jackhammer within 10-30 minutes. [Check the links/video ](https://jch.olacabs.com/userguide/adding_new_tool) 

## Quick Start and Installation

See our [Quick Start/Installation Guide][] if you want to try out Jackhammer as quickly as possible using Docker Compose.

##### Run the following commands for local setup (corporate mode):

```
 git clone https://github.com/olacabs/jackhammer
 sh ./docker-build.sh

```
##### Default credentials for local setup:

username: jackhammer@olacabs.com

password: j4ckh4mm3r

##### (For single user mode)
``` 
sh ./docker-build.sh SingleUser

```

do signup for access

## Restarting Jackhammer

```
docker-compose stop
docker-compose rm
docker-compose up -d
```
## User Guide

   The [User Guide][] will give you an overview of how to use Jackhammer once you have things up and running.

## Demo

##### Demo Environment Link: 
https://jch.olacabs.com/

##### Default credentials:

username: admin@admin.com

password: admin@admin.com

[Brakeman]: http://brakemanscanner.org/
[Bundler-Audit]: https://github.com/rubysec/bundler-audit
[Dawnscanner]: https://github.com/thesp0nge/dawnscanner
[FindSecurityBugs]: https://find-sec-bugs.github.io/
[Xanitizer*]: https://www.rigs-it.net/index.php/get-xanitizer.html
[NodeSecurityProject]: https://nodesecurity.io/
[Retire.js]: https://retirejs.github.io/retire.js/
[Trufflehog]: https://github.com/dxa4481/truffleHog
[Arachni]: http://www.arachni-scanner.com/
[Androbugs]: https://github.com/AndroBugs/AndroBugs_Framework
[Androguard]: https://github.com/androguard/androguard
[Nmap]: https://nmap.org/
[WPScan]: https://github.com/wpscanteam/wpscan
[User Guide]: https://jch.olacabs.com/userguide
[Quick Start/Installation Guide]: http://jch.olacabs.com/userguide/installation
