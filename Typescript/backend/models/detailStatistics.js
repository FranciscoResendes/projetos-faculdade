const mongoose = require('mongoose');

const QualwebInfoSchema = new mongoose.Schema({
  name: String,
  code: String,
  description: String,
  outcome: String,
  successCriteria: [String],
})

const DetailStatisticsSchema = new mongoose.Schema({
    idWebsite: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Website',
      required: true
  },
  idPage: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Page',
      required: true
  },
  totalPassedTests: Number,
  totalWarningTests: Number,
  totalFailedTests: Number,
  totalInapplicableTests: Number,
  actRulesTestsResults: [QualwebInfoSchema],
  wcagTestsResults: [QualwebInfoSchema],
});

module.exports = mongoose.model('DetailStatistics', DetailStatisticsSchema);