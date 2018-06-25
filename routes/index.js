var exec = require('child_process');
var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
 
 call_java(res);
 
 
  
});


function call_java(res){


  exec('java main conf.ini check_depth', (err, stdout, stderr) => {
    if (err) {
      console.error(err);
      return;
    }
    console.log(stdout);
  });


  res.render('index', { title: 'Express' });


}
module.exports = router;
