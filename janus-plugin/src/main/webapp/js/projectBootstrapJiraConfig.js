(function($, _) {
    var getJiraConfigName = function (element) {
        return $(element).parents('tr.dropdownList-container')
            .prev()
            .find('select')
            .val()
            .substring(5); // "jira-" prefix
    };

    /*
     * #######################################################################
     * GROUP SELECTION
     * #######################################################################
     */
    $('.janus-jira-group input').each(function () {
        var element = $(this),
            messageElement = document.createElement('div');
        messageElement.className = 'janus-info';
        messageElement.style.display = 'none';
        element.parent().append(messageElement);

        var updateGroupMessage = function () {
            action.getExistingGroups(getJiraConfigName(element), element.val(), function (resp) {
                var responseObject = resp.responseObject();

                if (responseObject.length != 0 && responseObject[0].name == element.val()) {
                    $(messageElement).text('This group already exists. Users will be added to it.')
                } else {
                    $(messageElement).text('A group with this name will be created for you.');
                }

                $(messageElement).slideDown();
            });
        };

        element.autocomplete({
            select: updateGroupMessage,
            source: function (query, callback) {
                action.getExistingGroups(getJiraConfigName(element), query.term, function (resp) {
                    var responseObject = resp.responseObject();

                    var result = [];
                    for (var i = 0; i < responseObject.length; i++) {
                        result.push(responseObject[i].name);
                    }

                    callback(result);
                });
            }
        });

        element.change(updateGroupMessage);
    });

    /*
     * #######################################################################
     * ADDED USERS OVERVIEW
     * #######################################################################
     */
     var userListingTemplate = _.template('<li>' +
        '<%=fullName%>' +
        '<span class=\'email\'><%=email%></span>' +
        '<span class=\'remove\'>x</span>' +
        '<input type=\'hidden\' name=\'_.userFullName\' value=\'<%=fullName%>\'/>' +
        '<input type=\'hidden\' name=\'_.userUsername\' value=\'<%=username%>\'/>' +
        '<input type=\'hidden\' name=\'_.userEmail\' value=\'<%=email%>\'/>' +
        '<input type=\'hidden\' name=\'_.userPassword\' value=\'<%=password%>\'/>' +
        '<input type=\'hidden\' name=\'_.userNew\' value=\'<%=newUser%>\'/>' +
        '</li>');

    $('.addedUsers').each(function() {
        var addedUsers = this.addedUsers = {};

        $(this).on('click', '.remove', function () {
            var element = $(this).parent();

            delete addedUsers[element.find('.email').text()];

            element.slideUp(function () {
                element.remove();
            });
        });
    });

    var addUserToListing = function (username, fullName, email, password, newUser) {
        var ul = $('.addedUsers:visible')[0],
            addedUsers = ul.addedUsers;

        if (addedUsers[email] !== undefined) {
            var li = addedUsers[email];
            $(li).effect('highlight', 1000);
            return;
        }

        var tmp = document.createElement('div');

        tmp.innerHTML = userListingTemplate({
            fullName: fullName,
            username: username,
            email: email,
            password: password,
            newUser: newUser
        });

        var li = tmp.childNodes[0];
        li.style.display = 'none';
        $(ul).append(li);
        $(li).slideDown().effect('highlight', 1000);
        addedUsers[email] = li;
    };



    /*
     * #######################################################################
     * EXISTING USER ADDITION
     * #######################################################################
     */
    $('.janus-jira-user-autocomplete input[type="text"]').each(function() {
        var inputExUser = this,
            addExUserButton = $(this).next();

        var setUserErrorMessage = function (msg) {
            var errorContainer = $(inputExUser).parent().children('error');

            if (errorContainer.length == 0) {
                var div = document.createElement('div');
                div.className = 'error';
                $(inputExUser).parent().append(div);
                errorContainer = $(div);
            }

            errorContainer.text(msg);
        };

        var removeUserErrorMessage = function () {
            $(inputExUser).parent().children('.error').hide();
        };

        $(inputExUser).keyup(removeUserErrorMessage);

        $(inputExUser).autocomplete({
            source: function (query, callback) {
                action.getExistingUsers(getJiraConfigName(inputExUser), query.term, function (resp) {
                    var responseObject = resp.responseObject();

                    var result = [];
                    for (var i = 0; i < responseObject.length; i++) {
                        var user = responseObject[i];
                        result.push({
                            label: user.name + ' <' + user.email + '>',
                            value: user.email
                        });
                    }

                    callback(result);
                });
            }
        });

        addExUserButton.click(function () {
            action.getExistingUsers(getJiraConfigName(inputExUser), inputExUser.value, function (resp) {
                var responseObject = resp.responseObject();

                if (responseObject.length == 0 || responseObject[0].email != inputExUser.value) {
                    setUserErrorMessage('No registered email address matches this query.');
                } else {
                    var user = responseObject[0];
                    addUserToListing(user.name, user.fullname, user.email, null, false);
                    inputExUser.value = '';
                    removeUserErrorMessage();
                }

            });
        });
    });

    /*
     * #######################################################################
     * NEW USER ADDITION
     * #######################################################################
     */
    var usernameInput = $('.janus-jira-new-user-username input'),
        fullNameInput = $('.janus-jira-new-user-fullName input'),
        emailInput = $('.janus-jira-new-user-email input'),
        passwordInput = $('.janus-jira-new-user-password input');

    var clearNewUserInputFields = function () {
        usernameInput.val('');
        fullNameInput.val('');
        emailInput.val('');
        passwordInput.val('');
    };

    var showMessages = function (messages) {
        var inputs = [usernameInput, fullNameInput, emailInput, passwordInput];

        for (var i = 0; i < messages.length; i++) {
            var message = messages[i], input = inputs[i].filter(':visible');

            var div = input.parent(),
                errorDiv = div.find('.error');

            if (message === null) {
                errorDiv.remove();
            } else {
                if (errorDiv.length == 0) {
                    errorDiv = document.createElement('div');
                    errorDiv.className = 'error';
                    div.append(errorDiv);
                    errorDiv = $(errorDiv);
                }

                errorDiv.text(message);
            }
        }
    };

    var validate = function (callback) {
        var username = usernameInput.filter(':visible').val(),
            fullName = fullNameInput.filter(':visible').val(),
            email = emailInput.filter(':visible').val(),
            password = passwordInput.filter(':visible').val(),
            jiraConfigName = getJiraConfigName(usernameInput.filter(':visible'));

        action.validateUserInput(jiraConfigName, username, fullName, email, password,
                                 function(resp) {
            var messages = resp.responseObject();
            showMessages(messages);

            if (callback !== undefined) {
                var allValid = _.all(messages, function (val) {
                        return val === null;
                    });
                callback(allValid);
            }
        });
    };

    usernameInput.change(validate);
    fullNameInput.change(validate);
    emailInput.change(validate);
    passwordInput.change(validate);

    $('body').on('click', '.addNewUser', function (event) {
        $(this).blur();

        validate(function (valid) {
            if (valid) {
                addUserToListing(usernameInput.filter(':visible').val(),
                            fullNameInput.filter(':visible').val(),
                            emailInput.filter(':visible').val(),
                            passwordInput.filter(':visible').val(),
                            true);
                clearNewUserInputFields();
            }
        });

        return false;
    });
})(window.jQuery, window.underscore);