const express = require('express');
const accessibilityController = require('../controllers/accessibilityController');

const router = express.Router();

router.patch('/api/evaluate', accessibilityController.evaluateWebsiteAccessibility);
router.get('/api/statistics/:websiteId', accessibilityController.getStatistics);
router.get('/api/detailedStatistics/:websiteId/:pageId', accessibilityController.getDetailedStatistics);

module.exports = router;