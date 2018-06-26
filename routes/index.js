var fs = require('fs');
const { exec } = require('child_process');
var path = require('path');

var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
 
var config_file = path.join(__dirname,path.sep,'..',path.sep,'public',path.sep,'config.ini');
var config_details = fs.readFileSync(config_file);

res.render('index', { title: 'Express', config_details:config_details });
 
  
});


module.exports = router;
