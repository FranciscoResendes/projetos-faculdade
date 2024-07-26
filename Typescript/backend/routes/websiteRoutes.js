const express = require('express');
const websiteController = require('../controllers/websitecontroller');

const router = express.Router();

router.post('/api/websites', websiteController.createWebsite);
router.get('/api/websites', websiteController.getAllWebsites);
router.get('/api/websites/:websiteId', websiteController.getWebsite);
router.get('/api/websites/:websiteId/:pageId', websiteController.getPage);
router.put('/api/websites/:WebsiteId', websiteController.AddPageToWebsite);
router.put('/api/websites/:WebsiteId/status', websiteController.updateWebsiteStatus);
router.delete('/api/websites',websiteController.deleteWebsite);
router.delete('/api/websites/pages', websiteController.deletePages);

module.exports = router;