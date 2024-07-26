const mongoose = require('mongoose');

const StatisticsSchema = new mongoose.Schema({
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
  hasErros: Boolean,
  hasErrosA: Boolean,
  hasErrosAA: Boolean,
  hasErrosAAA: Boolean,
  errorList: [String],
});

const Statistics = mongoose.model('Statistics', StatisticsSchema);

module.exports = Statistics;