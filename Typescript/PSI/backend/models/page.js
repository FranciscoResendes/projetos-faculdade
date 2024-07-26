const mongoose = require('mongoose');

const PageSchema = new mongoose.Schema({
  select: Boolean,
  id: Number,
  url: String,
  appraisalDate: {type: Date, default: Date.now},
  conformity: { type: String, default: 'NA' },
  status: { type: String, default: 'Por avaliar' },
  reportid: {type: Number, default: 0},},
  { versionKey: false }
);

const Page = mongoose.model('Page', PageSchema);

module.exports = Page;