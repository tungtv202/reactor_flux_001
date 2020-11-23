var faker = require('faker')
var path = require('path')
var faces = require('cool-ascii-faces').faces
var getRandomInRange = require('./util/get-random-in-range')
var moment = require('moment')
var repeat = require('./util/repeat-func')

function generateUser () {
  var email = faker.internet.email()
  var username = email.replace(/\@.*$/, '')

  return {
    username: username,
    email: email
  }
}

function generateProduct () {
  return {
    id: getRandomInRange(1, 999999),
    face: faces[getRandomInRange(0, faces.length)],
    price: getRandomInRange(1, 1234),
    size: getRandomInRange(12, 40)
  }
}

function generatePurchases (users, products, maxPurchasesPerUser) {
  var numProducts = products.length
  function generatePurchase (user) {
    return {
      id: getRandomInRange(1, 999999),
      username: user.username,
      productId: products[getRandomInRange(0, numProducts)].id,
      date: moment().subtract(getRandomInRange(1000, 999999), 'seconds').toISOString()
    }
  }

  return [].concat.apply([], users.map(function (user) {
    var numPurchases = getRandomInRange(0, maxPurchasesPerUser)
    return repeat(numPurchases, generatePurchase.bind(null, user))
  }))
}

function generateData () {
  var numUsers = 10
  var numProducts = 10
  var maxPurchasesPerUser = 10
  var users = repeat(numUsers, generateUser)
  var products = repeat(numProducts, generateProduct)
  var purchases = generatePurchases(users, products, maxPurchasesPerUser)

  console.log(users);
  return {
    users: users,
    products: products,
    purchases: purchases
  }
}

module.exports = function (dataPath) {
  // we'll try to use hardcoded data first, but if that is not present we'll generate a new set
  try {
    console.log(dataPath)
    return {
      users: require(path.join(dataPath, 'users.json')),
      products: require(path.join(dataPath, 'products.json')),
      purchases: require(path.join(dataPath, 'purchases.json')),
    }
  } catch (e) {
    console.log(e)
    console.log('- Generating new data')
    return generateData()
  }
}

module.exports.generateData = generateData
