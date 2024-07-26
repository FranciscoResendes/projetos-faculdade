const mongoose = require('mongoose');
const PageSchema = require('./page').schema;

const WebsiteSchema = new mongoose.Schema({
  url: String,
  pages: [PageSchema],
  submissionDate: {type: Date, default: Date.now},
  appraisalDate:{type: Date, default: Date.now},
  status: { type: String, default: 'Por avaliar' },},
  { versionKey: false }
);

const Website = mongoose.model('Website', WebsiteSchema);

module.exports = Website;