var path = require('path');
var fs=require('fs');

var express = require('express');
var router = express.Router();

const { exec } = require('child_process');


/* GET home page. */
router.get('/', function(req, res, next) {
 
  var option = req.query.option;
  console.log(req.query)

  
 call_java(option,res);
// res.render('index', { title: 'Express' });
 
  
});


function call_java(option,res){

  
  var main_class = path.join(__dirname,path.sep,'..',path.sep,'public',path.sep,'jar',path.sep,'MQ_java.jar');
  var config_file = path.join(__dirname,path.sep,'..',path.sep,'public',path.sep,'config.ini');
  
  // var option = 'put_data ';

  if(option == 'put_data')
    var command = 'java -jar  ' + main_class + ' ' + config_file + ' ' + option + ' ' + '\"hi .. this is a test data\"';
  else
    var command = 'java -jar  ' + main_class + ' ' + config_file + ' ' + option;
  

  console.log(command);

  exec(command, (err, stdout, stderr) => {
    if (err) {
      console.log(err);
      return;
    }

    var queue_depth = path.join(__dirname,path.sep,'..',path.sep,'queue_depth');
    

    var depth = fs.readFileSync(queue_depth)




    if(option == 'put_data')
      res.render('put_data')
    else
      res.render('select_option',{depth:depth})

    // console.log(stdout);
    // console.log(stderr);

  });


  


}
module.exports = router;
