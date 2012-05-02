(function($, _) {

    /*
     * #######################################################################
     * REQUIRED INPUT FIELD VALIDATION
     * #######################################################################
     */
    var getInput = function(wrapperId, type) {
        return document.getElementById(wrapperId)
            .getElementsByTagName(type)[0];
    };

    var addValidation = function(propertyName, inputType) {
        var input = getInput("janus-" + propertyName, inputType);
        var errorMsg = YAHOO.util.Dom.getNextSibling(input);
        var validation = function() {
            var value = input.value;
            var valMethod = 'isValid' + propertyName
                .charAt(0)
                .toUpperCase() + propertyName.slice(1);
            action[valMethod](value, function(rsp) {
                var valid = rsp.responseObject();
                if (valid) {
                    errorMsg.hide();
                } else {
                    errorMsg.show();
                }
            });
        };
        input.addEventListener('change',
                validation,
                false);
        validation();
    };

    var fields = [{name: 'name', type: 'input'},
            {name: 'pckg', type: 'input'},
            {name: 'description', type: 'textarea'}];
    for(var i = 0; i < fields.length; i++) {
        var field = fields[i];
        addValidation(field.name, field.type);
    }

    var requiredInputValidation = function(event) {
        var input = event.target;
        var errorMessage = YAHOO.util.Dom.getNextSibling(input);
        var value = YAHOO.lang.trim(input.value);
        if (value.length == 0) {
            errorMessage.show();
        } else {
            errorMessage.hide();
        }
    };

    var requiredInputs = YAHOO.util.Dom.getElementsByClassName('required');
    for(var i = 0; i < requiredInputs.length; i++) {
        var requiredInput = requiredInputs[i].getElementsByTagName('input')[0];
        requiredInput.addEventListener('change',
                requiredInputValidation,
                false);
        requiredInputValidation({target: requiredInput});
    }

    /*
     * #######################################################################
     * GROUP SELECTION
     * #######################################################################
     */
     $('.janus-jira-group input').each(function() {
        var element = this;

        var jiraConfigName = jQuery(element)
            .parents('table')
            .first()
            .prev('.janusConfigName')
            .text();

        var messageElement = document.createElement('div');
        messageElement.className = 'janus-info';
        messageElement.style.display = 'none';
        element.parentNode.appendChild(messageElement);

        $(element).autocomplete({
            source: function(query, callback) {
                action.getExistingGroups(jiraConfigName, query.term, function (resp) {
                    var responseObject = resp.responseObject();

                    var result = [];
                    for (var i = 0; i < responseObject.length; i++) {
                        result.push(responseObject[i].name);
                    }

                    callback(result);
                });
            }
        });

        $(element).change(function() {
            action.getExistingGroups(jiraConfigName, element.value, function (resp) {
                var responseObject = resp.responseObject();

                if (responseObject.length != 0 && responseObject[0].name == element.value) {
                    $(messageElement).text('This group already exists. Users will be added to it.')
                } else {
                    $(messageElement).text('A group with this name will be created for you.');
                }

                $(messageElement).slideDown();
            });
        });
     });

    /*
     * #######################################################################
     * ADDED USERS OVERVIEW
     * #######################################################################
     */
     var userListingTemplate = _.template('<li>' +
        '<%=name%>' +
        '<span class=\'email\'><%=email%></span>' +
        '<span class=\'remove\'>x</span>' +
        '</li>'),
        addedUsers = {};

     $('.addedUsers').on('click', '.remove', function() {
        var element = $(this).parent();

        delete addedUsers[element.find('.email').text()];

        element.slideUp(function() {
            element.remove();
        });
     });

     var addUserToListing = function (username, fullName, email, newUser) {
        if (addedUsers[email] !== undefined) {
            var li = addedUsers[email];
            $(li).effect('highlight', 1000);
            return;
        }

        var tmp = document.createElement('div');

        tmp.innerHTML = userListingTemplate({
            name: fullName,
            username: username,
            email: email,
            newUser: newUser
        });

        var li = tmp.childNodes[0];
        li.style.display = 'none';
        $('.addedUsers').append(li);
        $(li).slideDown().effect('highlight', 1000);
        addedUsers[email] = li;
     };

    /*
     * #######################################################################
     * EXISTING USER ADDITION
     * #######################################################################
     */
    var inputExUser = $('.janus-jira-user-autocomplete input[type="text"]')[0],
        jiraConfigName = jQuery(inputExUser)
                .parents('table')
                .first()
                .prev('.janusConfigName')
                .text();

    var setUserErrorMessage = function(msg) {
        var errorContainer = $(inputExUser).parent().children('error');

        if (errorContainer.length == 0) {
            var div = document.createElement('div');
            div.className = 'error';
            $(inputExUser).parent().append(div);
            errorContainer = $(div);
        }

        errorContainer.text(msg);
    };

    var removeUserErrorMessage = function() {
        $(inputExUser).parent().children('.error').hide();
    };

    jQuery(inputExUser).autocomplete({
        source: function(query, callback) {
            action.getExistingUsers(jiraConfigName, query.term, function (resp) {
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

    jQuery(inputExUser).keyup(function() {
        removeUserErrorMessage();
    });

    $('.janus-jira-user-autocomplete input[type="button"]').click(function() {
        action.getExistingUsers(jiraConfigName, inputExUser.value, function (resp) {
            var responseObject = resp.responseObject();

            if (responseObject.length == 0 || responseObject[0].email != inputExUser.value) {
                setUserErrorMessage('No registered email address matches this query.');
            } else {
                var user = responseObject[0];
                addUserToListing(user.name, user.fullname, user.email);
                inputExUser.value = '';
                removeUserErrorMessage();
            }

        });
    });
})(jQuery.noConflict(), window._.noConflict());