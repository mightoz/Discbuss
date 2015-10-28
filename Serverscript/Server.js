var Firebase = require("firebase");


/**
 * Created by joakim on 2015-10-18.
 */

var rootRef = new Firebase("https://boiling-heat-3778.firebaseio.com/");
var lastActivity = new Date();
var statementNumber = 0;
var statements = [];

rootRef.child("chatRooms").child("11").on("child_added", function(snapshot, prevChildKey){
    lastActivity = new Date();
    console.log("chatting registered, resetting time, current time: " + lastActivity.getTime());
});

rootRef.child("statements").on("child_added", function(snapshot, prevChildKey){
    statements.push(snapshot.val());
});


setInterval(function(){
    var d = new Date();
    if(lastActivity.getTime() < d.getTime()-30000){
        console.log("updating statement on firebase!");
        rootRef.child("chatRooms").child("currentStatement").set(statements[statementNumber%statements.length]);
        statementNumber = statementNumber + 1;
        lastActivity = new Date();
    }
}, 1000);
