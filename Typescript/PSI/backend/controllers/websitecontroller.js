const Website = require('../models/website');
const Page = require('../models/page');
const Statistics = require('../models/statistics');
const DetailStatistics = require('../models/detailStatistics');

exports.createWebsite = (req, res) => {
  const website = new Website({ url: req.body.url, pages: req.body.pageUrls || [] });
  website.save()
    .then((savedWebsite) => {
      res.status(200).json(savedWebsite);
    })
    .catch(err => {
      res.status(500).send(err);
    });
};

exports.getWebsite = (req, res) => {
  Website.findOne({ _id: req.params.websiteId })
    .then(website => {
      if (website) {
        res.status(200).json(website);
      } else {
        res.status(404).send("Website not found");
      }
    })
    .catch(err => {
      console.log(err);
      res.status(500).send(err);
    });
};

exports.getPage = (req, res) => {
  Website.findOne({ _id: req.params.websiteId })
    .then(website => {
      if (website) {
        const page = website.pages.find(page => page._id == req.params.pageId);
        if (page) {
          res.status(200).json(page);
        } else {
          res.status(404).send("Page not found");
        }
      } else {
        res.status(404).send("Website not found");
      }
    })
    .catch(err => {
      console.log(err);
      res.status(500).send(err);
    });
};

exports.AddPageToWebsite = (req, res) => {
  Website.findOne({ _id: req.params.WebsiteId })
  .then(website => {
    if (website) {
      if (!website.pages) {
        website.pages = [];
      }
      const page = new Page(req.body.page);
      website.pages.push(page);
      website.save()
        .then(() => {
          res.status(200).json("Página adicionada com sucesso!");
        })
        .catch(err => {
          console.log(err);
          res.status(500).send(err);
        });
    } else {
      res.status(404).send("Website not found");
    }
  })
  .catch(err => {
    console.log(err);
    res.status(500).send(err);
  });
};

exports.updateWebsiteStatus = async (req, res) => {
  const { WebsiteId } = req.params;
  const { status } = req.body;

  try {
    const updatedWebsite = await Website.findByIdAndUpdate(WebsiteId, { status: status }, { new: true });

    if (!updatedWebsite) {
      return res.status(404).json({ message: 'Website not found' });
    }

    res.json(updatedWebsite);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
};

exports.getAllWebsites = (req, res) => {
  Website.find()
    .then(websites => {
      res.status(200).json(websites);
    })
    .catch(err => {
      res.status(500).send(err);
    });
};

exports.deleteWebsite = (req, res) => {
  Website.findOneAndDelete({_id: req.body._id})
  .then(website => {
    if (website) {
      if (website.status === 'Avaliado') {

        // Delete the statistics for the website
        Statistics.deleteMany({ idWebsite: website._id })
        .then(() => {
          console.log('Statistics by website id deleted successfully');
          res.status(200).json(website);
        })
        .catch(err => {
          console.error('Error deleting statistics by website id:', err);
          res.status(500).send(err);
        });

        // Delete the detail statistics for the website
        DetailStatistics.deleteMany({ idWebsite: website._id })
        .then(() => {
          console.log('DetailStatistics by website id deleted successfully');
        })
        .catch(err => {
          console.error('Error deleting detail statistics by website id:', err);
        });

      } else {
        res.status(200).json(website);
      }
    } else {
      res.status(404).send("Website not found!");
    }
  })
  .catch(err => {
    console.error('Error deleting website:', err);
    res.status(500).send(err);
  });
};

exports.deletePages = (req, res) => {
  Website.findOne({ _id: req.body.webId })
  .then(website => {
      if (website) {

        for(const page of req.body.pages) {
          website.pages.remove(page)

          if(page.status === 'Avaliado'){

            // Delete the statistics for the page
            Statistics.findOneAndDelete({ idPage: page._id })
            .then(() => {
              console.log('Statistics by page id deleted successfully');
            })
            .catch(err => {
              console.error('Error deleting statistics by page id:', err);
              res.status(500).send(err);
            });

            // Delete the detail statistics for the page
            DetailStatistics.findOneAndDelete({ idPage: page._id })
            .then(() => {
              console.log('DetailStatistics by page id deleted successfully');
            })
            .catch(err => {
              console.error('Error deleting detail statistics by page id:', err);
            });

          }
        }
          website.save()
            .then(() => {
            res.status(200).json("Page Removed Sucessfully!");
        })


      } else {
      res.status(404).send("Website not found");
      }
  })
  .catch(err => {
      console.log(err);
      res.status(500).send(err);
  });
};
