# Task Manager (Incomplete)
A simple task manager and  project management system based on MySQL, PHP, and HTML.

Accounts, contacts, tasks (dubbed traces), and projects are stored in the database. Can be used as a very basic
CRM to track customer interaction as well as generate basic project management forms such as a WBS. To meet a personal need, the server is separated from the client. The mysql server can be run on the same host as the client, on a network host, or online.

This code was written on Ubuntu and OSX. The configuration instructions are for a linux environment, however the language choices (PHP and JAVA) were made to allow this to run on any environment.

NOTE: This was written in the shell. The Maven software development file structure was annoying to navigate, so I used a simplier directory structure.

This also includes Google's gson library v.2.8.6 to encode and decode JSON strings for the API. Google's library can be found at https://github.com/google/gson.

# Server Code
The files in the server code folder should be located on the same machine as the mysql server. The server code is written in PHP. All API requests go to the Services.php file and are parsed from there (http://ip-address/Services.php). To allow the easier tracking of changes, I recommend placing a symlink in the /var/www/html/ repository that points to the Services.php file in the git repository. The blankDB file holds the structure of the database. This can be uploaded into a MySQL instance to properly format the database.

*At the moment, user credentials have to be added manually in the MySQL database.

Configuration:
1.) Install MySQL server on the server.
2.) Create a task manager database.
3.) Create a user to manage the task manager database.
4.) Upload the blank database file to the task manager database.
5.) Modify the database credentials (/server_code/headers/credentials.php) to reflect the database access information. (At the moment, the server location field isn't used.)
6.) Insert a user account into the database.
7.) Generate a password hash and add it to the user table.
8.) Navigate to the /var/www/html/ directory and create a symlink that points to the Services.php file.

# Client Code
The Java client can be located on the same computer as the server or on a different device. Network access must be available and the MySQL server configured before executing the client code. The client includes basic features to allow for creation of "traces" or tasks, the ability to mark them as complete, the ability to create accounts, contacts, and projects and assign them to traces.


Roadmap: 
Version 1.0
 - Basic functionality
 - Create/edit tasks
 - Mark tasks as completed
 - Create/edit accounts
 - Create/edit contacts
 - Create/edit projects
 
 Version 1.1
  - Clean up trace functionality.
  - Allow multiple line input in trace details and account details
  - Assign contacts to traces
  - Sort traces by contacts.
  
 Version 1.2
  - Clean up account functionality
  - Use GridBagLayout to improve the contacts and project details
  - Include include_in_all_searches checkbox to create master account
  - Active/Inactive checkbox for Account, Contact, and Project.
  
 Version 1.3
  - Configuration Menu
  - Add users from client
  - Change password in client
  - Configure saved hosts.
  
 Version 2.0 - Reporting
 Version 3.0 - Expanded Project Management functionality
 
 
 Phase 2: Project Management
  - Read Projects from database
  - Add projects via HTML Interface
  - Add WBS elements via HTML Interface
  - Read WBS elements from database
  - Resequence WBS elements via HTML Interface
  - Read tasks associated with project via HTML
  
 Phase 3: Reporting
  - Report count of tasks completed by type for date range.
  - Report time spent on tasks by project and type for date range.
  - Report time spent on tasks by project and type for lifetime.
