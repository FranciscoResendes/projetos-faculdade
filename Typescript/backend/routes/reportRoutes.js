const express = require('express');
const router = express.Router();
const reportController = require('../controllers/reportController');

router.post('/api/reports/export', reportController.generateReport);

module.exports = router;