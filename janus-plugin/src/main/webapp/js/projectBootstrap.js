(function() {
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
})();
