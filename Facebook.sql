CREATE TABLE UserGood (
    UserName VARCHAR(50) PRIMARY KEY UNIQUE,
    Hashing VARCHAR(255) NOT NULL,
    Name VARCHAR(100) NOT NULL,
    DateOfBirth DATE NOT NULL,
    Gender ENUM('Male', 'Female', 'Other') NOT NULL,
    Street VARCHAR(255) NOT NULL,
    Town VARCHAR(100) NOT NULL,
    State VARCHAR(100) NOT NULL,
    Country VARCHAR(100) NOT NULL,
    Degree VARCHAR(100) NOT NULL,
    School VARCHAR(100) NOT NULL,
	Status ENUM('Active','Inactive') NOT NULL DEFAULT 'Active',
	profilePicture VARCHAR(512) NOT NULL
);

CREATE TABLE MenuElement (
    MenuId numeric PRIMARY KEY,
    Title VARCHAR(100) NOT NULL,
    Description VARCHAR(255) NOT NULL
);

CREATE TABLE Role (
    RoleId VARCHAR(50) NOT NULL,
    Name VARCHAR(50) NOT NULL,
    Description TEXT NOT NULL
	PRIMARY KEY (RoleId),
);

CREATE TABLE Friendship (
    FriendshipID int(10) NOT NULL,
    User1 VARCHAR(50) NOT NULL,
    User2 VARCHAR(50) NOT NULL,
    FriendshipDate DATE NOT NULL,
    PRIMARY KEY (FriendshipID),
    FOREIGN KEY (User1) REFERENCES UserGood(UserName) ON DELETE CASCADE,
    FOREIGN KEY (User2) REFERENCES UserGood(UserName) ON DELETE CASCADE
);

CREATE TABLE RoleUserGood (
    UserName VARCHAR(50) NOT NULL,
    RoleId VARCHAR(50) NOT NULL,
    DateAssign DATE NOT NULL,
    PRIMARY KEY (UserName, RoleId),
    FOREIGN KEY (UserName) REFERENCES UserGood(UserName) ON DELETE CASCADE,
	FOREIGN KEY (RoleId) REFERENCES Role(RoleId) ON DELETE CASCADE
);

CREATE TABLE WebPageGood (
    PageURL VARCHAR(255) NOT NULL,
    pageTitle VARCHAR(100) NOT NULL,
    Description VARCHAR(100) NOT NULL,
    MenuID numeric,
	PRIMARY KEY (PageURL),
    FOREIGN KEY (MenuId) REFERENCES MenuElement(MenuId) ON DELETE CASCADE
);

CREATE TABLE WebPagePrevious (
    CurrentPageURL VARCHAR(255) NOT NULL,
    PreviousPageURL VARCHAR(255) NOT NULL,
    PRIMARY KEY (CurrentPageURL, PreviousPageURL),
    FOREIGN KEY (CurrentPageURL) REFERENCES WebPageGood(PageURL) ON DELETE CASCADE,
    FOREIGN KEY (PreviousPageURL) REFERENCES WebPageGood(PageURL) ON DELETE CASCADE
);

CREATE TABLE RoleWebPageGood (
    RoleId VARCHAR(50) NOT NULL,
    PageURL VARCHAR(255) NOT NULL,
    dateAssign DATE NOT NULL,
    PRIMARY KEY (RoleId, PageURL),
    FOREIGN KEY (RoleId) REFERENCES Role(RoleId) ON DELETE CASCADE,
    FOREIGN KEY (PageURL) REFERENCES WebPageGood(PageURL) ON DELETE CASCADE
);




INSERT INTO MenuElement VALUES (1, 'Users', 'User management');
INSERT INTO MenuElement VALUES (2, 'General', 'General operations');
INSERT INTO MenuElement VALUES (3, 'Help', 'Technical support');
INSERT INTO MenuElement VALUES (0, 'General pages', 'General system pages for user actions and utilities');
INSERT INTO MenuElement VALUES (4, 'Account', 'Personal account settings');

INSERT INTO role value ("rol1", "administrator", "administrator");
INSERT INTO role value ("rol3", "student", "users");


INSERT INTO WebPageGood VALUES ('addFriend.jsp', 'Add Friend', 'Adds a friend to user list', 0);
INSERT INTO WebPageGood VALUES ('addRoleUser.jsp', 'Adds Role', 'Adds role to new user', 0);
INSERT INTO WebPageGood VALUES ('addRoleUserAdmin.jsp', 'Add Role Admin', 'Assign role after admin user creation', 0);
INSERT INTO WebPageGood VALUES ('addUser.jsp', 'Add User', 'This page adds users to the system', 0);
INSERT INTO WebPageGood VALUES ('addUserAdmin.jsp', 'Add User Admin', 'Admin-only user registration page', 0);
INSERT INTO WebPageGood VALUES ('friendList.jsp', 'Friend List', 'Page to list user friends', 2);
INSERT INTO WebPageGood VALUES ('ModifyUser.jsp', 'Modify User Info', 'Shows form to change users data', 0);
INSERT INTO WebPageGood VALUES ('ModifyUserAction.jsp', 'Modify Execution', 'This page executes the modification of user account', 0);
INSERT INTO WebPageGood VALUES ('ModifyUserAdmin.jsp', 'Modify User', 'Show all user that can be modified', 1);
INSERT INTO WebPageGood VALUES ('newUserAdmin.jsp', 'New User', 'Page to create a new User', 1);
INSERT INTO WebPageGood VALUES ('profile.jsp', 'My Profile', 'Page to show the user''s account information', 4);
INSERT INTO WebPageGood VALUES ('removeUser.jsp', 'Remove User Action', 'This page removes users from the system', 0);
INSERT INTO WebPageGood VALUES ('removeUserAdmin.jsp', 'Remove User', 'Shows all user that can be removed', 1);
INSERT INTO WebPageGood VALUES ('removeUserPic.jsp', 'Remove Picture', 'Removes users friend and puts default', 0);
INSERT INTO WebPageGood VALUES ('searchFriend.jsp', 'Search Friend', 'Page to search for friends', 2);
INSERT INTO WebPageGood VALUES ('searchUser.jsp', 'Search User', 'Page to show users to be friend', 0);
INSERT INTO WebPageGood VALUES ('upload_action.jsp', 'Upload Picture Action', 'Upload picture to database', 0);
INSERT INTO WebPageGood VALUES ('upload.jsp', 'Upload Picture', 'User can upload a picture', 0);
INSERT INTO WebPageGood VALUES ('validationHashing.jsp', 'Validation', 'The validation page', 0);
INSERT INTO WebPageGood VALUES ('welcomeMenu.jsp', 'Welcome', 'The welcome page', 0);


INSERT INTO WebPagePrevious VALUES ('welcomeMenu.jsp', 'addFriend.jsp');
INSERT INTO WebPagePrevious VALUES ('welcomeMenu.jsp', 'addRoleUserAdmin.jsp');
INSERT INTO WebPagePrevious VALUES ('addRoleUserAdmin.jsp', 'addUserAdmin.jsp');
INSERT INTO WebPagePrevious VALUES ('welcomeMenu.jsp', 'friendList.jsp');
INSERT INTO WebPagePrevious VALUES ('ModifyUserAction.jsp', 'ModifyUser.jsp');
INSERT INTO WebPagePrevious VALUES ('ModifyUserAdmin.jsp', 'ModifyUser.jsp');
INSERT INTO WebPagePrevious VALUES ('removeUserPic.jsp', 'ModifyUser.jsp');
INSERT INTO WebPagePrevious VALUES ('welcomeMenu.jsp', 'ModifyUserAction.jsp');
INSERT INTO WebPagePrevious VALUES ('ModifyUser.jsp', 'ModifyUserAdmin.jsp');
INSERT INTO WebPagePrevious VALUES ('welcomeMenu.jsp', 'ModifyUserAdmin.jsp');
INSERT INTO WebPagePrevious VALUES ('addUserAdmin.jsp', 'newUserAdmin.jsp');
INSERT INTO WebPagePrevious VALUES ('welcomeMenu.jsp', 'newUserAdmin.jsp');
INSERT INTO WebPagePrevious VALUES ('welcomeMenu.jsp', 'profile.jsp');
INSERT INTO WebPagePrevious VALUES ('welcomeMenu.jsp', 'removeUser.jsp');
INSERT INTO WebPagePrevious VALUES ('removeUser.jsp', 'removeUserAdmin.jsp');
INSERT INTO WebPagePrevious VALUES ('welcomeMenu.jsp', 'removeUserAdmin.jsp');
INSERT INTO WebPagePrevious VALUES ('ModifyUserAdmin.jsp', 'removeUserPic.jsp');
INSERT INTO WebPagePrevious VALUES ('searchUser.jsp', 'searchFriend.jsp');
INSERT INTO WebPagePrevious VALUES ('welcomeMenu.jsp', 'searchFriend.jsp');
INSERT INTO WebPagePrevious VALUES ('addFriend.jsp', 'searchUser.jsp');
INSERT INTO WebPagePrevious VALUES ('searchFriend.jsp', 'searchUser.jsp');
INSERT INTO WebPagePrevious VALUES ('ModifyUserAdmin.jsp', 'upload_action.jsp');
INSERT INTO WebPagePrevious VALUES ('profile.jsp', 'upload_action.jsp');
INSERT INTO WebPagePrevious VALUES ('upload_action.jsp', 'upload.jsp');
INSERT INTO WebPagePrevious VALUES ('welcomeMenu.jsp', 'validationHashing.jsp');
INSERT INTO WebPagePrevious VALUES ('addUser.jsp', 'welcomeMenu.jsp');
INSERT INTO WebPagePrevious VALUES ('friendList.jsp', 'welcomeMenu.jsp');
INSERT INTO WebPagePrevious VALUES ('ModifyUserAdmin.jsp', 'welcomeMenu.jsp');
INSERT INTO WebPagePrevious VALUES ('newUserAdmin.jsp', 'welcomeMenu.jsp');
INSERT INTO WebPagePrevious VALUES ('profile.jsp', 'welcomeMenu.jsp');
INSERT INTO WebPagePrevious VALUES ('removeUserAdmin.jsp', 'welcomeMenu.jsp');
INSERT INTO WebPagePrevious VALUES ('searchFriend.jsp', 'welcomeMenu.jsp');


INSERT INTO rolewebpagegood VALUES ('rol1', 'addFriend.jsp', '2025-03-16');
INSERT INTO rolewebpagegood VALUES ('rol1', 'addRoleUser.jsp', '2025-03-20');
INSERT INTO rolewebpagegood VALUES ('rol1', 'addRoleUserAdmin.jsp', '2025-03-25');
INSERT INTO rolewebpagegood VALUES ('rol1', 'addUser.jsp', '2025-03-13');
INSERT INTO rolewebpagegood VALUES ('rol1', 'addUserAdmin.jsp', '2025-03-25');
INSERT INTO rolewebpagegood VALUES ('rol1', 'friendList.jsp', '2025-03-13');
INSERT INTO rolewebpagegood VALUES ('rol1', 'ModifyUser.jsp', '2025-03-21');
INSERT INTO rolewebpagegood VALUES ('rol1', 'ModifyUserAction.jsp', '2025-03-22');
INSERT INTO rolewebpagegood VALUES ('rol1', 'ModifyUserAdmin.jsp', '2025-03-22');
INSERT INTO rolewebpagegood VALUES ('rol1', 'newUserAdmin.jsp', '2025-03-20');
INSERT INTO rolewebpagegood VALUES ('rol1', 'profile.jsp', '2025-03-18');
INSERT INTO rolewebpagegood VALUES ('rol1', 'removeUser.jsp', '2025-03-13');
INSERT INTO rolewebpagegood VALUES ('rol1', 'removeUserAdmin.jsp', '2025-03-23');
INSERT INTO rolewebpagegood VALUES ('rol1', 'removeUserPic.jsp', '2025-03-25');
INSERT INTO rolewebpagegood VALUES ('rol1', 'searchFriend.jsp', '2025-03-13');
INSERT INTO rolewebpagegood VALUES ('rol1', 'searchUser.jsp', '2025-03-16');
INSERT INTO rolewebpagegood VALUES ('rol1', 'upload_action.jsp', '2025-03-22');
INSERT INTO rolewebpagegood VALUES ('rol1', 'upload.jsp', '2025-03-22');
INSERT INTO rolewebpagegood VALUES ('rol1', 'welcomeMenu.jsp', '2025-03-13');

INSERT INTO rolewebpagegood VALUES ('rol3', 'addFriend.jsp', '2025-03-16');
INSERT INTO rolewebpagegood VALUES ('rol3', 'friendList.jsp', '2025-03-13');
INSERT INTO rolewebpagegood VALUES ('rol3', 'profile.jsp', '2025-03-18');
INSERT INTO rolewebpagegood VALUES ('rol3', 'searchFriend.jsp', '2025-03-13');
INSERT INTO rolewebpagegood VALUES ('rol3', 'searchUser.jsp', '2025-03-16');
INSERT INTO rolewebpagegood VALUES ('rol3', 'welcomeMenu.jsp', '2025-03-13');

INSERT INTO usergood values ('admin', 'ac9689e2272427085e35b9d3e3e8bed88cb3434828b43b86fc0596cad4c6e270', 'Administrator', '2002-08-05','Male','Calle 184','Cayey','Puerto Rico','USA','Computer Engineer','UAGM', 'Active', 'Facebook/Pictures/default_pfp.jpg');
INSERT INTO roleusergood values ('admin', 'rol3', '2025/03/16');