import * as React from 'react';
import { useEffect, useState, useRef } from 'react';
import Button from '@mui/material/Button';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { AppBar, Toolbar} from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import { Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import axios from "axios";
import { useParams } from 'react-router-dom';
import DeleteIcon from '@mui/icons-material/Delete';
import IconButton from '@mui/material/IconButton';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper
} from '@mui/material';

import {
  Container,
  Typography,
  Box,
  Card,
  CardContent,
  Grid,
  Avatar,
} from '@mui/material';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import PersonIcon from '@mui/icons-material/Person';
import EventIcon from '@mui/icons-material/Event';
import CancelScheduleSendIcon from '@mui/icons-material/CancelScheduleSend';

const defaultTheme = createTheme();

export default function TrainingView() {
  const [korisnicko_ime, setKorisnickoIme] = useState('');

  const token = localStorage.getItem('jwtToken'); // Get JWT token from localStorage
  const [email, setEmail] = useState('');
  const [role, setRole] = useState('');

  const navigate = useNavigate();
  const isMounted = useRef(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [dialogMessage, setDialogMessage] = useState('');
  const navigate2 = useNavigate(); // React Router's navigate function to redirect

const toDateObject = (dateArray) => {
  if (!Array.isArray(dateArray)) return null;
  const [year, month, day, hour, minute] = dateArray;
  return new Date(year, month - 1, day, hour, minute); // JS months are 0-based
};

  const { id } = useParams();
  const [training, setTraining] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios.get(`http://localhost:8080/trainings/${id}`)
      .then(response => {
        setTraining(response.data);
        setLoading(false);
      })
      .catch(() => {
        setLoading(false);
      });
  }, [id]);


  
  const handleCloseDialog = () => {
    setOpenDialog(false);
    navigate2('/login');
  };

  const logout = () => {
    localStorage.removeItem("jwtToken"); // Remove token

    // Redirect to login page
    window.location.href = "/login"; 
  };

    
  useEffect(() => {
      if (!token) {
        setDialogMessage('No user found. Please log in.');
        setOpenDialog(true);
        setTimeout(() => {
          navigate('/login'); // Redirect to login after 15 seconds
        }, 15000); // Delay redirection to allow user to read the message
        return;
      }
  
      fetch('http://localhost:8080/auth/decodeJwt', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: token,
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to decode token');
        }
        return response.json();
      })
      .then(data => {
        if (!data || data.Role !== "TRAINER") {
          setDialogMessage('Unauthorized access. Redirecting to login...');
          setOpenDialog(true);
          setTimeout(() => {
            navigate('/login');
          }, 15000);
          return;
        }
  
        // Set user data only if role is valid
        setEmail(data.Email);
        setKorisnickoIme(data.Username);
        setRole(data.Role);
      })
      .catch(error => {
        console.error('Error decoding JWT token:', error);
        setDialogMessage('Session expired or invalid token. Please log in again.');
        setOpenDialog(true);
        setTimeout(() => {
          navigate('/login');
        }, 15000);
      });
  }, [token, navigate]);


const handleCancelBooking = (username) => {
  if (!training || !training.id || !training.startTime || training.cancelDeadline == null) {
    alert("Training data is incomplete.");
    return;
  }

  // Calculate deadline time
  const startTime = toDateObject(training.startTime);
  const now = new Date();
  const cancelDeadlineHours = parseInt(training.cancelDeadline, 10);
  const latestCancelTime = new Date(startTime.getTime() - cancelDeadlineHours * 60 * 60 * 1000);

  // Check if current time is before cancel deadline
  if (now > latestCancelTime) {
    alert(`You can no longer cancel this booking. Cancellations must be made at least ${training.cancelDeadline} hours before the training starts.`);
    return;
  }

  if (window.confirm("Are you sure you want to cancel this booking?")) {
    axios
      .delete(`http://localhost:8080/trainings/${training.id}/bookings/${username}`)
      .then(() => {
        const updatedUserList = training.users.filter(user => user.username !== username);
        setTraining(prev => ({ ...prev, users: updatedUserList }));

        alert("Booking successfully cancelled.");
      })
      .catch(error => {
        console.error("Error cancelling booking:", error);

        const message =
          error.response?.data?.message ||
          error.response?.data?.error ||
          "An error occurred while cancelling the booking.";
        alert(message);
      });
  }
};



  useEffect(() => {
    isMounted.current = true;
    return () => {
      isMounted.current = false;
    };
  }, []);


  return (
    <div>
      {/* Dialog box for showing the message */}
      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>Notification</DialogTitle>
        <DialogContent>{dialogMessage}</DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="primary">
            OK
          </Button>
        </DialogActions>
      </Dialog>
    <ThemeProvider theme={defaultTheme}>
      <AppBar position="static" sx={{ bgcolor: '#4FC3F7' }}>
        <Toolbar sx={{ justifyContent: 'space-between' }}>
          <Link to="/" style={{ textDecoration: 'none', display: 'flex', alignItems: 'center', color: 'inherit' }}>
            <Box sx={{ display: 'flex', alignItems: 'center', ml: 2 }}>
          <Avatar sx={{ m: 1, bgcolor: '#283593' }}>
          <FitnessCenterIcon />
        </Avatar>               
        <Typography variant="h5" component="div" sx={{ fontWeight: 'bold' }}>
                FitnessApp
              </Typography>
            </Box>
          </Link>
            <Box sx={{ display: 'flex', gap: 2 }}>
              <Button component={Link} to="/bookedTrainings" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                All trainings
              </Button>
              <Button component={Link} to="/bookTrainingTrainer" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                Book training
              </Button>
              <Button component={Link} to="/cancelTrainingTrainer" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                Cancel training
              </Button>
              {token && korisnicko_ime ? ( 
                <Button onClick={logout} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold'}}>
                  Logout
                </Button>
              ) : (<></>)}
            </Box>
          <Box sx={{ display: 'flex', gap: 2, mr: 2, alignItems: 'center' }}>
            {token && korisnicko_ime ? ( 
              <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                Welcome, {korisnicko_ime}
              </Typography>
            ) : (
              <>
                <Button component={Link} to="/registration" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  Sign In
                </Button>
                <Button component={Link} to="/login" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  Log In
                </Button>
              </>
            )}
          </Box>
        </Toolbar>
      </AppBar>
              {training ? (
      <Container maxWidth="lg">
        <Box sx={{ mt: 6 }}>
          <Card sx={{ p: 3, borderRadius: 3, boxShadow: 4 }}>
            <CardContent>
        {/* Existing Training Info */}
        <Grid container spacing={2} alignItems="center" justifyContent="center">
          <Grid item>
            <Avatar sx={{ bgcolor: 'primary.main', width: 56, height: 56 }}>
              <FitnessCenterIcon />
            </Avatar>
          </Grid>
          <Grid item>
            <Typography variant="h4" gutterBottom>
              {training.trainingType}
            </Typography>
          </Grid>
        </Grid>

        <Box sx={{ mt: 3 }}>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <EventIcon sx={{ mr: 1 }} />
                <Typography variant="subtitle1">
                  {toDateObject(training.startTime).toLocaleString('en-US', {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit',
                  })}
                </Typography>
              </Box>
            </Grid>

            <Grid item xs={12} sm={6}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <AccessTimeIcon sx={{ mr: 1 }} />
                <Typography variant="subtitle1">
                  Duration: {training.duration} min
                </Typography>
              </Box>
            </Grid>

            <Grid item xs={12} sm={6}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <PersonIcon sx={{ mr: 1 }} />
                <Typography variant="subtitle1">
                  Trainer: {training.trainer}
                </Typography>
              </Box>
            </Grid>

            <Grid item xs={12} sm={6}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <CancelScheduleSendIcon sx={{ mr: 1 }} />
                <Typography variant="subtitle1">
                  Cancel up to {training.cancelDeadline} hrs before
                </Typography>
              </Box>
            </Grid>
          </Grid>
        </Box>

        {/* Booked Users Table */}
        <Box sx={{ mt: 5 }}>
          <Typography variant="h6" gutterBottom>
            Booked users
          </Typography>
          {training.users && training.users.length > 0 ? (
            <TableContainer component={Paper} sx={{ borderRadius: 2, boxShadow: 3 }}>
            <Table>
                <TableHead>
                <TableRow sx={{ backgroundColor: '#4FC3F7' }}>
                    <TableCell><strong>#</strong></TableCell>
                    <TableCell><strong>Username</strong></TableCell>
                    <TableCell><strong>Name</strong></TableCell>
                    <TableCell><strong>Surname</strong></TableCell>
                    <TableCell><strong>Email</strong></TableCell>
                    <TableCell><strong>Phone number</strong></TableCell>
                    <TableCell align="center"><strong>Actions</strong></TableCell> {/* New column */}
                </TableRow>
                </TableHead>
                <TableBody>
                {training.users.map((user, index) => (
                    <TableRow
                    key={user.id || index}
                    sx={{
                        '&:nth-of-type(odd)': { backgroundColor: 'grey.100' },
                        '&:hover': { backgroundColor: 'grey.200' },
                    }}
                    >
                    <TableCell>{index + 1}</TableCell>
                    <TableCell>{user.username}</TableCell>
                    <TableCell>{user.name}</TableCell>
                    <TableCell>{user.surname}</TableCell>
                    <TableCell>{user.email}</TableCell>
                    <TableCell>{user.phoneNumber}</TableCell>
                    <TableCell align="center">
                        <IconButton
                        color="error"
                        onClick={() => handleCancelBooking(user.username)}
                        aria-label="cancel booking"
                        >
                        <DeleteIcon />
                        </IconButton>
                    </TableCell>
                    </TableRow>
                ))}
                </TableBody>

              </Table>
            </TableContainer>
          ) : (
            <Typography variant="body2" sx={{ mt: 2 }}>
              No users have booked this training yet.
            </Typography>
          )}
        </Box>
      </CardContent>
    </Card>
  </Box>
</Container>


) : (
  <Typography variant="h6" align="center" sx={{ mt: 6 }}>
    Loading training data...
  </Typography>
)}


    </ThemeProvider>
    </div>
  );
}
