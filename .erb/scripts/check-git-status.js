const {execSync} = require('child_process')

let buffer = execSync('git status --porcelain=v1')
let string = buffer.toString()
console.log(string)
